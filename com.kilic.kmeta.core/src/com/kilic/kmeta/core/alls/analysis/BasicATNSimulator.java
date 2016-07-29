package com.kilic.kmeta.core.alls.analysis;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
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
import com.kilic.kmeta.core.util.CharSet;

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

		RegularCallStack cs = RegularCallStack.newAnyStack();
		if (((ATN) atnState.getContainer()).getAllCallers().size() > 1)
			cs.push(g.peek());

		if (atnState.getPredictionDFA() == null) {
			PredictionDFA predictionDFA = new PredictionDFA();
			predictionDFA.setLabel("S" + atnState.getLabel() + "PDFA");
			atnState.setPredictionDFA(predictionDFA);
		}

		PredictionDFA dfa = atnState.getPredictionDFA();
		if (dfa.getStartState(cs) == null) {
			ATNConfigSet configSet = computePredictionDFAStartState(atnState, cs);
			PredictionDFAState startState = dfa.createState(configSet);
			dfa.setStartState(cs, startState);

			IATNEdge finalEdge = null;
			for (ATNConfig config : configSet) {
				if (config.getState().isFinalState()) {
					startState.setFinal(config.getAlternative());
					if (finalEdge == null)
						finalEdge = config.getAlternative();
					else if (finalEdge != config.getAlternative()) {
						// conflict
						System.out.println("Conflict in start state!");
					}
				}
			}
		}
		IATNEdge result = sllPredict(atnState, (PredictionDFAState) dfa.getStartState(cs), cs, pos);

		input.seek(pos);
		return result;
	}

	IATNEdge sllPredict(ATNState atnState, PredictionDFAState d0, RegularCallStack g, int offset) {
		PredictionDFAState d = d0;
		while (true) {
			//System.out.println("Predicting using sll cache " + d0.getContainer().getLabel());
			//System.out.println("next input chars: " + input.lookAheadString(0, 6));

			PredictionDFAState next = d.move(input);

			if (next == null) {
				next = target(d);
				//System.out.println(next.getKey());
			} else {
				//System.out.println("Cache hit: " + next);
			}

			if (next.getType() == StateType.ERROR)
				return null; // error

			if (next.isStackSensitive()) {
				//System.out.println("Prediction for " + atnState + " / " + g.toString()
				//		+ " is stack sensitive. Falling back to LL.");
				return llPredict(atnState, g, offset);
			}

			if (next.getType() == StateType.FINAL) {
				//System.out.println(
				//		"Predicted " + next.getDecisionEdge() + " using sll cache " + d0.getContainer().getLabel());
				return next.getDecisionEdge();
			}

			d = next;
			input.skip(1);
		}
	}

	IATNEdge llPredict(ATNState atnState, RegularCallStack g, int offset) {
		ATNConfigSet currentConfigSet = computePredictionDFAStartState(atnState, g);

		while (true) {
			ATNConfigSet nextConfigSet = moveAndGetClosures(currentConfigSet);

			if (nextConfigSet.isEmpty()) {
				System.out.println("SYNTAX ERROR: " + input.lookAheadString(0, 6));
				return null;
			}

			IATNEdge predictedEdge = nextConfigSet.allMembersHaveSameAlternative();
			if (predictedEdge != null) {
				//System.out.println("Predicted " + predictedEdge + " for state " + atnState + " using llPredict");
				return predictedEdge;
			}

			Set<Set<IATNEdge>> altSets = getConflictSetsPerLoc(nextConfigSet);
			Set<IATNEdge> conflictingAlts = null;
			boolean conflict = true;
			for (Set<IATNEdge> altSet : altSets) {
				if (altSet.size() <= 1) {
					conflict = false;
					break;
				}
				if (conflictingAlts == null)
					conflictingAlts = altSet;
				else if (!conflictingAlts.equals(altSet)) {
					conflict = false;
					break;
				}
			}

			if (conflict) {
				System.out.println("CONFLICT: " + conflictingAlts);
				return conflictingAlts.iterator().next();
			}

			input.skip(1);
			currentConfigSet = nextConfigSet;
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

		if (input.hasEnded()) {
			if (d.getType() == StateType.FINAL)
				return d;
		}

		PredictionDFAState matchingState = null;
		Map<CharSet, Set<IATNEdge>> dcss = d.getKey().getNextDistinctCharSets();
		for (Entry<CharSet, Set<IATNEdge>> dcs : dcss.entrySet()) {
			if (!dcs.getKey().containsSingleton(input.lookAheadChar(0)))
				continue;
			ATNConfigSet newStateKey = unionClosures(d.getKey().moveByEdges(dcs.getValue()));
			PredictionDFAState state = dfa.getState(newStateKey);

			if (state == null) {
				state = dfa.createState(newStateKey);
				//System.out.println(dfa.getLabel() + " new state " + state + " for " + dcs.getKey());
				// System.out.println(newStateKey);
			}
			dfa.createEdge(d, state, dcs.getKey());

			IATNEdge predictedEdge = newStateKey.allMembersHaveSameAlternative();
			if (predictedEdge != null)
				state.setFinal(predictedEdge);
			else {
				boolean conflict = containsASetWithMultipleElements(getConflictSetsPerLoc(newStateKey));
				boolean hasViableAlt = containsASetWithSingleElement(getProdSetsPerLoc(newStateKey));

				if (conflict && !hasViableAlt)
					state.setStackSensitivity(true);
			}

			matchingState = state;
			//System.out.println(dfa.getLabel() + " matching state " + state);
		}

		if (matchingState == null) {
			//System.out.println("Syntax Error: expecting " + dcss.keySet() + " but got " + input.lookAheadChar(0));
			//System.out.println(dfa.toGraphviz());
			return dfa.getErrorState();
		}

		return matchingState;
	}

	private boolean containsASetWithMultipleElements(Set<Set<IATNEdge>> setOfSets) {
		for (Set<?> set : setOfSets) {
			if (set.size() > 1)
				return true;
		}

		return false;
	}

	private boolean containsASetWithSingleElement(Set<Set<IATNEdge>> setOfSets) {
		for (Set<?> set : setOfSets) {
			if (set.size() == 1)
				return true;
		}

		return false;
	}

	private ATNConfigSet moveAndGetClosures(ATNConfigSet d) {
		return unionClosures(d.move(input));
	}

	private ATNConfigSet unionClosures(ATNConfigSet d) {
		ATNConfigSet newConfigSet = new ATNConfigSet();
		for (ATNConfig config : d)
			newConfigSet.addAll(closure(config, null));
		return newConfigSet;
	}

	Set<Set<IATNEdge>> getConflictSetsPerLoc(ATNConfigSet configSet) {
		Set<Set<IATNEdge>> result = new HashSet<>();

		for (ATNState state : configSet.getAllStates()) {
			Map<RegularCallStack, Set<IATNEdge>> altsByCallStack = new HashMap<>();

			for (ATNConfig config : configSet.getConfigsByState(state)) {
				Set<IATNEdge> altSet = altsByCallStack.get(config.getCallStack());
				if (altSet == null) {
					altSet = new HashSet<>();
					altsByCallStack.put(config.getCallStack(), altSet);
				}

				altSet.add(config.getAlternative());
			}

			result.addAll(altsByCallStack.values());
		}

		return result;
	}

	Set<Set<IATNEdge>> getProdSetsPerLoc(ATNConfigSet configSet) {
		Set<Set<IATNEdge>> result = new HashSet<>();

		for (ATNState state : configSet.getAllStates()) {
			Set<IATNEdge> alternativesByState = new HashSet<>();
			for (ATNConfig config : configSet.getConfigsByState(state))
				alternativesByState.add(config.getAlternative());

			result.add(alternativesByState);
		}
		return result;
	}
}
