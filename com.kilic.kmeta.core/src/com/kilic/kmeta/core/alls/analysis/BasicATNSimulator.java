package com.kilic.kmeta.core.alls.analysis;

import java.util.HashSet;
import java.util.Set;

import com.kilic.kmeta.core.alls.atn.ATN;
import com.kilic.kmeta.core.alls.atn.ATNCallEdge;
import com.kilic.kmeta.core.alls.atn.ATNEpsilonEdge;
import com.kilic.kmeta.core.alls.atn.ATNState;
import com.kilic.kmeta.core.alls.atn.IATNEdge;
import com.kilic.kmeta.core.alls.predictiondfa.PredictionDFA;
import com.kilic.kmeta.core.alls.predictiondfa.PredictionDFAState;
import com.kilic.kmeta.core.alls.stream.IStream;
import com.kilic.kmeta.core.alls.tn.IState.StateType;

/**
 * Algorithm from:
 * 
 * PARR T., HARWELL S., FISHER K. Adaptive LL(*) Parsing: The Power of Dynamic
 * Analysis
 * 
 * This simulator does not use GSS.
 * 
 */
public class BasicATNSimulator {
	IStream input;

	public BasicATNSimulator(IStream input) {
		this.input = input;
	}

	// returns the start state of the predicted alternative
	public IATNEdge adaptivePredict(ATNState atnState, RegularCallStack g) {
		int pos = input.getPosition();

		if (atnState.getPredictionDFA() == null) {
			PredictionDFA predictionDFA = new PredictionDFA();
			predictionDFA.setLabel("S" + atnState.getLabel() + "PDFA");
			atnState.setPredictionDFA(predictionDFA);

			for (IATNEdge edge : atnState.getOut())
				predictionDFA.createFinalState(edge);

			ATNConfigSet configSet = computePredictionDFAStartState(atnState, RegularCallStack.newAnyStack());
			PredictionDFAState startState = predictionDFA.createState(configSet);
			predictionDFA.setStartState(startState);
		}

		PredictionDFA dfa = atnState.getPredictionDFA();
		IATNEdge result = sllPredict(atnState, (PredictionDFAState) dfa.getStartState(), g, pos);

		input.seek(pos);
		return result;
	}
	
	IATNEdge sllPredict(ATNState atnState, PredictionDFAState d0, RegularCallStack g, int offset) {
		PredictionDFAState d = d0;
		while (true) {
			PredictionDFAState next = d.move(input);
			if (next == null)
				next = target(d);
			if (next.getType() == StateType.ERROR)
				return null; // error
			boolean isStackSensitive = false;
			if (isStackSensitive)
				return llPredict(atnState, g, offset);

			if (next.getType() == StateType.FINAL)
				return next.getDecisionEdge();

			d = next;
			input.nextChar();
		}
	}

	IATNEdge llPredict(ATNState atnState, RegularCallStack g, int offset) {
		ATNConfigSet acs = computePredictionDFAStartState(atnState, g);
		while (true) {
			Set<ATNConfig> newConfigSet = moveAndGetClosures(acs);

			if (newConfigSet.isEmpty())
				return null;

			Set<Set<Integer>> altSets = getConflictSetsPerLoc(newConfigSet);
			// if ambiguity report return min(x)
		}
	}

	ATNConfigSet computePredictionDFAStartState(ATNState atnState, RegularCallStack g) {
		ATNConfigSet d0 = new ATNConfigSet();

		for (IATNEdge edge : atnState.getOut()) {
			if (edge instanceof ATNCallEdge) {
				ATNCallEdge callEdge = (ATNCallEdge) edge;
				RegularCallStack pushedStack = new RegularCallStack(g);
				pushedStack.push((ATNState) edge.getTo());
				d0.addAll(closure(new ATNConfig(callEdge.getATN().getStartState(), edge, pushedStack), null));
			} else if (edge instanceof ATNEpsilonEdge) {
				Set<ATNConfig> closure = closure(new ATNConfig((ATNState) edge.getTo(), edge, g), null);
				d0.addAll(closure);
			}
		}

		return d0;
	}

	Set<ATNConfig> closure(ATNConfig config, Set<ATNConfig> history) {
		if (history == null)
			history = new HashSet<>();
		else if (history.contains(config))
			return new HashSet<ATNConfig>();

		Set<ATNConfig> result = new HashSet<>();
		result.add(config);
		history.add(config);

		if (config.getState().getType() == StateType.FINAL) {
			// stack is SLL wildcard
			if (config.getCallStack().isAny()) {
				// get all returns of call for this type
				ATN atn = (ATN) config.getState().getContainer();
				for (ATNCallEdge edge : atn.getAllCallers()) {
					Set<ATNConfig> c = closure(
							new ATNConfig(edge.getTo(), config.getAlternative(), config.getCallStack()), history);
					result.addAll(c);
				}
			} else {
				// nonempty SLL or LL stack
				RegularCallStack poppedStack = new RegularCallStack(config.getCallStack());
				ATNState popped = poppedStack.pop();

				result.addAll(closure(new ATNConfig(popped, config.getAlternative(), poppedStack), history));
			}

			return result;
		} else {
			for (IATNEdge out : config.getState().getOut()) {
				if (out instanceof ATNCallEdge) {
					ATNCallEdge callEdge = (ATNCallEdge) out;

					RegularCallStack pushedStack = new RegularCallStack(config.getCallStack());
					pushedStack.push(out.getTo());

					result.addAll(closure(
							new ATNConfig(callEdge.getATN().getStartState(), config.getAlternative(), pushedStack),
							history));
				} else if (out instanceof ATNEpsilonEdge) {
					result.addAll(closure(new ATNConfig(out.getTo(), config.getAlternative(), config.getCallStack()),
							history));
				}
			}
		}

		return result;
	}

	PredictionDFAState target(PredictionDFAState d) {
		PredictionDFA dfa = (PredictionDFA) d.getContainer();

		String longestMatch = "";
		IATNEdge longestMatchingEdge = null;
		for (IATNEdge te : d.getKey().getNextTerminalEdges()) {
			String match = te.match(input);
			
			if (match!=null && match.length() > longestMatch.length()) {
				longestMatch = match;
				longestMatchingEdge = te;
			}
		}

		ATNConfigSet newConfigSet = moveAndGetClosures(d.getKey());
		if (newConfigSet.isEmpty()) {
			System.out.println("Error: " + input.toString() + " - " + d.toString());
			dfa.createEdge(d, dfa.getErrorState(), longestMatchingEdge);
			return dfa.getErrorState();
		}

		IATNEdge predictedEdge = null;
		boolean predictionDone = true;
		for (ATNConfig config : newConfigSet) {
			if (predictedEdge == null) {
				predictedEdge = config.getAlternative();
				continue;
			}

			if (predictedEdge != config.getAlternative()) {
				predictionDone = false;
				break;
			}
		}

		if (predictionDone) {
			PredictionDFAState f = dfa.getFinalState(predictedEdge);
			dfa.createEdge(d, f, longestMatchingEdge);
			System.out.println(dfa.toString());
			return f;
		} else {
			PredictionDFAState newState = dfa.createState(newConfigSet);
			dfa.createEdge(d, newState, longestMatchingEdge);
			System.out.println(dfa.toString());
			return newState;
		}
	}

	private ATNConfigSet moveAndGetClosures(ATNConfigSet d) {
		ATNConfigSet mv = d.move(input);
		ATNConfigSet newConfigSet = new ATNConfigSet();
		for (ATNConfig config : mv)
			newConfigSet.addAll(closure(config, null));
		return newConfigSet;
	}

	Set<Set<Integer>> getConflictSetsPerLoc(Set<ATNConfig> configSet) {
		return null;
	}
}
