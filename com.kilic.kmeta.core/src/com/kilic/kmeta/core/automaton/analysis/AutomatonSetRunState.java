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
import com.kilic.kmeta.core.automaton.CharSetMatcher;
import com.kilic.kmeta.core.automaton.IAutomatonTransition;
import com.kilic.kmeta.core.automaton.IMatcher;
import com.kilic.kmeta.core.automaton.MatcherTransition;
import com.kilic.kmeta.core.discriminator.CharSet;

// used to keep the states of parallel execution set of automatons.
class AutomatonSetRunState {
	public Map<Automaton, Set<AutomatonRunState>> automatonSetStates = new HashMap<>();

	public AutomatonSetRunState(Set<Automaton> automatons) {
		for (Automaton a : automatons) {
			automatonSetStates.put(a, getAllStatesWithCallsAndReturns(new AutomatonRunState(a)));
		}
	}

	public AutomatonSetRunState(AutomatonSetRunState other) {
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
		if (currentLocalState.isFinalState() ) {
			result.add(state);
		
			if(state.getCallStack().size() > 1) {
				AutomatonRunState newRunState = new AutomatonRunState(state);
				newRunState.returnFromCall();
				result.addAll(getAllStatesWithCallsAndReturns(newRunState));
			}
		}

		return result;
	}

	public Set<IMatcher> getAllMatchers() {
		Set<IMatcher> result = new HashSet<>();

		for (Set<AutomatonRunState> set : automatonSetStates.values()) {
			for (AutomatonRunState runState : set) {
				for (MatcherTransition t : IntersectionComputer.getMatcherTransitions(runState)) {
					result.add(t.getMatcher());
				}
			}
		}

		return result;
	}

	public void applyMatcher(IMatcher matcher) {
		Set<Automaton> deadAutomatons = new HashSet<>();

		for (Map.Entry<Automaton, Set<AutomatonRunState>> entry : this.automatonSetStates.entrySet()) {
			Set<AutomatonRunState> deadRuns = new HashSet<>();
			Set<AutomatonRunState> newRuns = new HashSet<>();
			Automaton automaton = entry.getKey();
			Set<AutomatonRunState> runStates = entry.getValue();

			for (AutomatonRunState runState : runStates) {
				AutomatonRunState newState = new AutomatonRunState(runState);
				if (matcher instanceof CharSetMatcher) {
					CharSet charSet = ((CharSetMatcher) matcher).getCharSet();
					if (IntersectionComputer.moveRunStateByIntersection(newState, charSet))
						newRuns.addAll(getAllStatesWithCallsAndReturns(newState));
					deadRuns.add(runState);
				}
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
		for (Set<AutomatonRunState> set : automatonSetStates.values()) {
			for (AutomatonRunState state : set) {
				if (state.isFinal())
					finalCount++;
				
				if(finalCount>1)
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
		if (obj instanceof AutomatonSetRunState) {
			AutomatonSetRunState other = (AutomatonSetRunState) obj;
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
