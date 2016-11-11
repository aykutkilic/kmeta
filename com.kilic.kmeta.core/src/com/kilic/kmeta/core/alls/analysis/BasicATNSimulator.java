package com.kilic.kmeta.core.alls.analysis;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.kilic.kmeta.core.alls.atn.ATN;
import com.kilic.kmeta.core.alls.atn.ATNCallEdge;
import com.kilic.kmeta.core.alls.atn.ATNEpsilonEdge;
import com.kilic.kmeta.core.alls.atn.ATNMutatorEdge;
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
	final IStream input;

	public BasicATNSimulator(IStream input) {
		this.input = input;
	}

	// returns the start state of the predicted alternative
	public IATNEdge adaptivePredict(final ATNState atnState, final RegularCallStack g) {
		final int pos = input.getPosition();

		final RegularCallStack cs = RegularCallStack.newAnyStack();
		if (((ATN) atnState.getContainer()).getAllCallers().size() > 1)
			cs.push(g.peek());

		if (atnState.getPredictionDFA() == null) {
			final PredictionDFA predictionDFA = new PredictionDFA();
			predictionDFA.setLabel("S" + atnState.getLabel() + "PDFA");
			atnState.setPredictionDFA(predictionDFA);
		}

		final PredictionDFA dfa = atnState.getPredictionDFA();
		if (dfa.getStartState(cs) == null) {
			final ATNConfigSet configSet = computePredictionDFAStartState(atnState, cs);
			final PredictionDFAState startState = dfa.createState(configSet);
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

		final IATNEdge result = sllPredict(atnState, (PredictionDFAState) dfa.getStartState(cs), cs, pos);

		input.seek(pos);
		return result;
	}

	private IATNEdge sllPredict(final ATNState atnState, final PredictionDFAState d0, final RegularCallStack g,
			final int offset) {
		PredictionDFAState d = d0;
		while (true) {
			PredictionDFAState next = d.move(input);

			if (next == null)
				next = target(d);

			if (next.getType() == StateType.ERROR)
				return null; // error

			if (next.isStackSensitive())
				return llPredict(atnState, g, offset);

			if (next.getType() == StateType.FINAL)
				return next.getDecisionEdge();

			d = next;
			input.skip(1);
		}
	}

	private IATNEdge llPredict(final ATNState atnState, final RegularCallStack g, final int offset) {
		ATNConfigSet currentConfigSet = computePredictionDFAStartState(atnState, g);

		while (true) {
			final ATNConfigSet nextConfigSet = moveAndGetClosures(currentConfigSet);

			if (nextConfigSet.isEmpty()) {
				System.out.println("SYNTAX ERROR: " + input.lookAheadString(0, 6));
				return null;
			}

			final IATNEdge predictedEdge = nextConfigSet.allMembersHaveSameAlternative();
			if (predictedEdge != null)
				return predictedEdge;

			final Set<Set<IATNEdge>> altSets = getConflictSetsPerLoc(nextConfigSet);
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

	ATNConfigSet computePredictionDFAStartState(final ATNState atnState, final RegularCallStack g) {
		final ATNConfigSet d0 = new ATNConfigSet();

		for (final IATNEdge edge : atnState.getOut()) {
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

	Set<ATNConfig> closure(final ATNConfig config, Set<ATNConfig> history) {
		if (history == null)
			history = new HashSet<>();
		else if (history.contains(config))
			return new HashSet<ATNConfig>();

		final Set<ATNConfig> result = new HashSet<>();
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
				final RegularCallStack poppedStack = new RegularCallStack(config.getCallStack());
				final ATNState popped = poppedStack.pop();

				result.addAll(closure(new ATNConfig(popped, config.getAlternative(), poppedStack), history));
			}

			return result;
		} else {
			for (final IATNEdge out : config.getState().getOut()) {
				if (out instanceof ATNCallEdge) {
					final ATNCallEdge callEdge = (ATNCallEdge) out;

					final RegularCallStack pushedStack = new RegularCallStack(config.getCallStack());
					pushedStack.push(out.getTo());

					result.addAll(closure(
							new ATNConfig(callEdge.getATN().getStartState(), config.getAlternative(), pushedStack),
							history));
				} else if (out instanceof ATNEpsilonEdge || out instanceof ATNMutatorEdge) {
					result.addAll(closure(new ATNConfig(out.getTo(), config.getAlternative(), config.getCallStack()),
							history));
				}
			}
		}

		return result;
	}

	PredictionDFAState target(final PredictionDFAState d) {
		final PredictionDFA dfa = (PredictionDFA) d.getContainer();

		if (input.hasEnded()) {
			if (d.getType() == StateType.FINAL)
				return d;
		}

		PredictionDFAState matchingState = null;
		final Map<CharSet, Set<IATNEdge>> dcss = d.getKey().getNextDistinctCharSets();
		for (Entry<CharSet, Set<IATNEdge>> dcs : dcss.entrySet()) {
			if (!dcs.getKey().containsSingleton(input.lookAheadChar(0)))
				continue;
			final ATNConfigSet newStateKey = unionClosures(d.getKey().moveByEdges(dcs.getValue()));
			PredictionDFAState state = dfa.getState(newStateKey);

			if (state == null)
				state = dfa.createState(newStateKey);

			dfa.createEdge(d, state, dcs.getKey());

			final IATNEdge predictedEdge = newStateKey.allMembersHaveSameAlternative();
			if (predictedEdge != null)
				state.setFinal(predictedEdge);
			else {
				final boolean conflict = containsASetWithMultipleElements(getConflictSetsPerLoc(newStateKey));
				final boolean hasViableAlt = containsASetWithSingleElement(getProdSetsPerLoc(newStateKey));

				if (conflict && !hasViableAlt)
					state.setStackSensitivity(true);
			}

			matchingState = state;
			// System.out.println(dfa.getLabel() + " matching state " + state);
		}

		if (matchingState == null) {
			// System.out.println("Syntax Error: expecting " + dcss.keySet() + "
			// but got " + input.lookAheadChar(0));
			// System.out.println(dfa.toGraphviz());
			return dfa.getErrorState();
		}

		return matchingState;
	}

	private boolean containsASetWithMultipleElements(final Set<Set<IATNEdge>> setOfSets) {
		for (final Set<?> set : setOfSets) {
			if (set.size() > 1)
				return true;
		}

		return false;
	}

	private boolean containsASetWithSingleElement(final Set<Set<IATNEdge>> setOfSets) {
		for (final Set<?> set : setOfSets) {
			if (set.size() == 1)
				return true;
		}

		return false;
	}

	private ATNConfigSet moveAndGetClosures(final ATNConfigSet d) {
		return unionClosures(d.move(input));
	}

	private ATNConfigSet unionClosures(final ATNConfigSet d) {
		final ATNConfigSet newConfigSet = new ATNConfigSet();
		for (final ATNConfig config : d)
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
