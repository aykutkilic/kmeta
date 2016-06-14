package com.kilic.kmeta.core.automaton.analysis;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.kilic.kmeta.core.automaton.Automaton;
import com.kilic.kmeta.core.automaton.AutomatonRunState;
import com.kilic.kmeta.core.automaton.AutomatonState;
import com.kilic.kmeta.core.automaton.CallAutomatonTransition;
import com.kilic.kmeta.core.automaton.IAutomatonTransition;
import com.kilic.kmeta.core.automaton.ICallStackElement;
import com.kilic.kmeta.core.automaton.MatcherTransition;

// used to keep the states of parallel execution set of automatons.
class AutomatonSetRunState {
	public Map<Automaton, Set<AutomatonRunState>> automatonSetStates = new HashMap<>();

	public AutomatonSetRunState(Set<Automaton> automatons) {
		for (Automaton a : automatons) {
			automatonSetStates.put(a, getAllStatesWithCallsAndReturn(new AutomatonRunState(a)));
		}
	}

	public AutomatonSetRunState(AutomatonSetRunState other) {
		automatonSetStates = new HashMap<>(other.automatonSetStates);
	}

	public Map<Automaton, Set<AutomatonRunState>> getStates() {
		return automatonSetStates;
	}

	public Set<AutomatonRunState> getAllStatesWithCallsAndReturn(AutomatonRunState state) {
		Set<AutomatonRunState> result = new HashSet<>();

		ICallStackElement currentLocalState = state.getCurrentLocalState();

		if(currentLocalState instanceof TransitionSubState) {
			result.add(state);
		} else {
			AutomatonState currentState = (AutomatonState)currentLocalState;
			// adding calls
			for (IAutomatonTransition t : currentState.getOutgoingTransitions()) {
				if (t instanceof MatcherTransition) {
					result.add(state);
				}
	
				if (t instanceof CallAutomatonTransition) {
					AutomatonRunState newRunState = new AutomatonRunState(state);
					newRunState.call((CallAutomatonTransition) t);
					result.add(newRunState);
				}
			}
	
			// Adding return
			if (currentState.isFinalState() && state.getCallStack().size() > 1) {
				AutomatonRunState newRunState = (AutomatonRunState) state.getCallStack().clone();
				newRunState.returnFromCall();
	
				result.add(newRunState);
			}
		}

		return result;
	}

	public Set<MatcherTransition> getAllMatcherTransitions() {
		Set<MatcherTransition> result = new HashSet<>();

		for (Set<AutomatonRunState> set : automatonSetStates.values()) {
			for (AutomatonRunState runState : set) {
				ICallStackElement localState = runState.getCurrentLocalState();
				if(localState instanceof AutomatonState) {
					for (IAutomatonTransition t : ((AutomatonState) localState).getOutgoingTransitions()) {
						if (t instanceof MatcherTransition) {
							result.add((MatcherTransition) t);
						}
					}
				} else if(localState instanceof TransitionSubState) {
					IAutomatonTransition transition = ((TransitionSubState) localState).getTransition();
					if(transition instanceof MatcherTransition)
						result.add((MatcherTransition) transition);
				}
			}
		}

		return result;
	}

	public void applyMatcherTransition(MatcherTransition transition) {
		Set<Automaton> deadAutomatons = new HashSet<>();

		for (Map.Entry<Automaton, Set<AutomatonRunState>> set : automatonSetStates.entrySet()) {
			Set<AutomatonRunState> deadRuns = new HashSet<>();
			Set<AutomatonRunState> newRuns = null;
			Automaton automaton = set.getKey();
			Set<AutomatonRunState> runStates = set.getValue();

			for (AutomatonRunState runState : runStates) {
				ICallStackElement localState = runState.getCurrentLocalState();
				ICallStackElement newState = localState.move(transition);
				if (newState == null)
					deadRuns.add(runState);
				else {
					runState.setCurrentLocalState(newState);
					newRuns = getAllStatesWithCallsAndReturn(runState);
				}
			}

			runStates.removeAll(deadRuns);
			if (newRuns != null)
				runStates.addAll(newRuns);

			if (runStates.isEmpty())
				deadAutomatons.add(automaton);
		}

		for (Automaton dead : deadAutomatons) {
			automatonSetStates.remove(dead);
		}
	}

	public boolean hasIntersection() {
		for (Set<AutomatonRunState> set : automatonSetStates.values()) {
			for (AutomatonRunState state : set) {
				if (!state.isFinal())
					return false;
			}
		}

		return true;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();

		result.append("[[\n");
		for (Entry<Automaton, Set<AutomatonRunState>> entry : automatonSetStates.entrySet()) {
			String label = entry.getKey().getLabel();
			if (label != null)
				result.append("  " + label + " {\n");
			else
				result.append("  {\n");

			for (AutomatonRunState state : entry.getValue())
				result.append("    " + state.toString() + "\n");
			result.append("  }\n");
		}

		result.append("]]\n");

		return result.toString();
	}
}
