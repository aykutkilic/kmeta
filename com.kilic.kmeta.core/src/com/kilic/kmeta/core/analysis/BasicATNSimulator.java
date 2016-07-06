package com.kilic.kmeta.core.analysis;

import java.util.HashSet;
import java.util.Set;

import com.kilic.kmeta.core.atn.ATN;
import com.kilic.kmeta.core.atn.ATNCallEdge;
import com.kilic.kmeta.core.atn.ATNConfig;
import com.kilic.kmeta.core.atn.ATNConfigSet;
import com.kilic.kmeta.core.atn.ATNEpsilonEdge;
import com.kilic.kmeta.core.atn.ATNState;
import com.kilic.kmeta.core.atn.IATNEdge;
import com.kilic.kmeta.core.atn.RegularCallStack;
import com.kilic.kmeta.core.dfa.DFA;
import com.kilic.kmeta.core.dfa.DFAState;
import com.kilic.kmeta.core.stream.IStream;

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
			DFA predictionDFA = new DFA();
			atnState.setPredictionDFA(predictionDFA);
			
			for(IATNEdge edge : atnState.getOutEdges())
				predictionDFA.createFinalState(edge);

			ATNConfigSet configSet = startState(atnState, RegularCallStack.newAnyStack());
			DFAState startState = predictionDFA.createState(configSet);
			predictionDFA.setStartState(startState);
		}

		DFA dfa = atnState.getPredictionDFA();
		IATNEdge result = sllPredict(atnState, dfa.getStartState(), g, pos);

		input.seek(pos);
		return result;
	}

	ATNConfigSet startState(ATNState atnState, RegularCallStack g) {
		ATNConfigSet d0 = new ATNConfigSet();
		
		for (IATNEdge edge : atnState.getOutEdges()) {
			if (edge instanceof ATNCallEdge) {
				ATNCallEdge callEdge = (ATNCallEdge) edge;
				RegularCallStack pushedStack = new RegularCallStack(g);
				pushedStack.push(edge.getTo());
				d0.addAll(closure(new ATNConfig(callEdge.getATN().getStartState(), edge, pushedStack), null));
			} else if (edge instanceof ATNEpsilonEdge) {
				d0.addAll(closure(new ATNConfig(edge.getTo(), edge, g), null));
			}
		}
		
		return d0;
	}

	Set<ATNConfig> closure(ATNConfig config, Set<ATNConfig> history) {
		if (history == null)
			history = new HashSet<>();
		else if (history.contains(config))
			return null;

		Set<ATNConfig> result = new HashSet<>();
		result.add(config);

		if (config.getState().isFinal()) {
			// stack is SLL wildcard
			if (config.getCallStack().isAny()) {
				// get all returns of call for this type
				ATN atn = config.getState().getATN();
				for (ATNCallEdge edge : atn.getAllCallers()) {
					result.addAll(closure(new ATNConfig(edge.getTo(), config.getAlternative(), config.getCallStack()),
							history));
				}
			} else {
				// nonempty SLL or LL stack
				RegularCallStack poppedStack = new RegularCallStack(config.getCallStack());
				ATNState popped = poppedStack.pop();

				result.addAll(closure(new ATNConfig(popped, config.getAlternative(), poppedStack), history));
			}

			return result;
		} else {
			for (IATNEdge out : config.getState().getOutEdges()) {
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

	DFAState target(DFAState d) {
		DFA dfa = d.getDFA();

		Set<ATNConfig> newConfigSet = getAllClosuresOfMove(d.getConfigSet());
		if (newConfigSet.isEmpty()) {
			// connect transition to error
			// but how? there's no tokenization :/
			// maybe I create a charset and add the char as a singleton
			// but that'd also be computationally expensive
			return d.getDFA().getErrorState();
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
			DFAState finalState = dfa.getFinalState(predictedEdge);
			return null;
		}
		
		return null;
	}

	IATNEdge sllPredict(ATNState atnState, DFAState d0, RegularCallStack g, int offset) {
		DFAState d = d0;
		while (true) {
			DFAState next = d.move(input);
			if (next == null)
				next = target(d);
			if (next.isErrorState())
				return null; // error
			boolean isStackSensitive = false;
			if (isStackSensitive)
				return llPredict(atnState, g, offset);

			if (d.isFinalState())
				return d.getDecisionEdge(); // i

			d = next;
			input.nextChar();
		}
	}

	IATNEdge llPredict(ATNState atnState, RegularCallStack g, int offset) {
		ATNConfigSet acs = startState(atnState, g);
		while (true) {
			Set<ATNConfig> newConfigSet = getAllClosuresOfMove(acs);

			if (newConfigSet.isEmpty())
				return null;

			Set<Set<Integer>> altSets = getConflictSetsPerLoc(newConfigSet);
			// if ambiguity report return min(x)
		}
	}

	private Set<ATNConfig> getAllClosuresOfMove(ATNConfigSet d) {
		ATNConfigSet mv = d.move(input);
		Set<ATNConfig> newConfigSet = new HashSet<>();
		for (ATNConfig config : mv)
			newConfigSet.addAll(closure(config, null));
		return newConfigSet;
	}

	Set<Set<Integer>> getConflictSetsPerLoc(Set<ATNConfig> configSet) {
		return null;
	}
}
