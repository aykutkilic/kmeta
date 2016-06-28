package com.kilic.kmeta.core.analysis;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;

import com.kilic.kmeta.core.dfa.Automaton;
import com.kilic.kmeta.core.dfa.AutomatonRunState;
import com.kilic.kmeta.core.dfa.AutomatonState;
import com.kilic.kmeta.core.dfa.CallAutomatonTransition;
import com.kilic.kmeta.core.dfa.CharSetMatcher;
import com.kilic.kmeta.core.dfa.IAutomatonTransition;
import com.kilic.kmeta.core.dfa.IMatcher;
import com.kilic.kmeta.core.dfa.MatcherTransition;
import com.kilic.kmeta.core.util.CharSet;

// used to keep the states of parallel execution set of automatons.
class ParallelAutomatonRunState {
	public Map<Automaton, Set<AutomatonRunState>> automatonSetStates = new HashMap<>();

	public ParallelAutomatonRunState(Set<Automaton> automatons) {
		for (Automaton a : automatons) {
			automatonSetStates.put(a, getAllStatesWithCallsAndReturns(new AutomatonRunState(a)));
		}
	}

	public ParallelAutomatonRunState(ParallelAutomatonRunState other) {
		automatonSetStates = new HashMap<>();
		for (Entry<Automaton, Set<AutomatonRunState>> entry : other.automatonSetStates.entrySet()) {
			Set<AutomatonRunState> newSet = new HashSet<>();
			for (AutomatonRunState runState : entry.getValue()) {
				newSet.add(new AutomatonRunState(runState));
			}
			automatonSetStates.put(entry.getKey(), newSet);
		}
	}

	public Map<Automaton, Set<AutomatonRunState>> getStates() {
		return automatonSetStates;
	}

	public Set<AutomatonRunState> getAllStatesWithCallsAndReturns(AutomatonRunState state) {
		Set<AutomatonRunState> result = new HashSet<>();

		AutomatonState currentLocalState = state.getCurrentLocalState();

		// adding calls
		for (IAutomatonTransition t : currentLocalState.getOutgoingTransitions()) {
			if (t instanceof MatcherTransition) {
				result.add(state);
			}

			if (t instanceof CallAutomatonTransition) {
				AutomatonRunState newRunState = new AutomatonRunState(state);
				newRunState.call((CallAutomatonTransition) t);
				result.addAll(getAllStatesWithCallsAndReturns(newRunState));
			}
		}

		// Adding return
		if (currentLocalState.isFinalState()) {
			result.add(state);

			if (state.getCallStack().size() > 1) {
				AutomatonRunState newRunState = new AutomatonRunState(state);
				newRunState.returnFromCall();
				result.addAll(getAllStatesWithCallsAndReturns(newRunState));
			}
		}

		return result;
	}

	public Set<CharSet> getAllMatchers() {
		Set<CharSet> result = new HashSet<>();

		for (Set<AutomatonRunState> set : automatonSetStates.values()) {
			for (AutomatonRunState runState : set) {
				for (MatcherTransition t : IntersectionComputer.getMatcherTransitions(runState)) {
					IMatcher m = t.getMatcher();
					if( m instanceof CharSetMatcher ) {
						result.add(((CharSetMatcher) m).getCharSet());
					} else
						assert(false);
				}
			}
		}

		return result;
	}

	public void applyCharSet(CharSet charSet) {
		Set<Automaton> deadAutomatons = new HashSet<>();

		for (Map.Entry<Automaton, Set<AutomatonRunState>> entry : this.automatonSetStates.entrySet()) {
			Set<AutomatonRunState> deadRuns = new HashSet<>();
			Set<AutomatonRunState> newRuns = new HashSet<>();
			Automaton automaton = entry.getKey();
			Set<AutomatonRunState> runStates = entry.getValue();

			for (AutomatonRunState runState : runStates) {
				AutomatonRunState newState = new AutomatonRunState(runState);
				if (IntersectionComputer.moveRunStateByIntersection(newState, charSet))
					newRuns.addAll(getAllStatesWithCallsAndReturns(newState));
				deadRuns.add(runState);
			}

			runStates.removeAll(deadRuns);
			runStates.addAll(newRuns);

			if (runStates.isEmpty())
				deadAutomatons.add(automaton);
		}

		for (Automaton dead : deadAutomatons) {
			automatonSetStates.remove(dead);
		}
	}

	public boolean hasIntersection() {
		int finalCount = 0;
		Set<AutomatonRunState> finalStates = new HashSet<>();
		for (Set<AutomatonRunState> set : automatonSetStates.values()) {
			for (AutomatonRunState state : set) {
				if (state.isFinal()) {
					// check if call stacks overlap
					boolean overlapping = false;
					for (AutomatonRunState finalState : finalStates) {
						Stack<AutomatonState> a = state.getCallStack();
						Stack<AutomatonState> b = finalState.getCallStack();

						overlapping = true;
						Iterator<AutomatonState> ai = a.iterator();
						Iterator<AutomatonState> bi = b.iterator();
						while (ai.hasNext() && bi.hasNext()) {
							if (!ai.next().equals(bi.next())) {
								overlapping = false;
								break;
							}
						}

						if (overlapping) {
							finalStates.remove(state);
							finalStates.remove(finalState);
							if (a.size() > b.size())
								finalStates.add(state);
							else
								finalStates.add(finalState);
							break;
						}
					}

					finalStates.add(state);
					if (!overlapping)
						finalCount++;
				}

				if (finalCount > 1)
					return true;
			}
		}

		return false;
	}

	@Override
	public int hashCode() {
		return automatonSetStates.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ParallelAutomatonRunState) {
			ParallelAutomatonRunState other = (ParallelAutomatonRunState) obj;
			return automatonSetStates.equals(other.automatonSetStates);
			/*
			 * AutomatonSetRunState other = (AutomatonSetRunState) obj; for
			 * (Entry<Automaton, Set<AutomatonRunState>> entry :
			 * automatonSetStates.entrySet()) { Set<AutomatonRunState>
			 * otherValue = other.automatonSetStates.get(entry.getKey());
			 * Set<AutomatonRunState> value = entry.getValue(); if
			 * (!value.equals(otherValue)) return false; }
			 * 
			 * return true;
			 */
		}
		return false;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();

		result.append("[[\n");
		for (Entry<Automaton, Set<AutomatonRunState>> entry : automatonSetStates.entrySet()) {
			String label = entry.getKey().getLabel();
			if (label != null)
				result.append("  " + label + " {");
			else
				result.append(" { ");

			for (AutomatonRunState state : entry.getValue())
				result.append(state.toString() + " ");
			result.append("}\n");
		}

		result.append("]]\n");

		return result.toString();
	}
}
