package com.kilic.kmeta.core.dfa;

import java.util.HashSet;
import java.util.Set;

public class AutomatonStateSet {
	Set<AutomatonState> states = new HashSet<>();

	public Set<AutomatonState> getStates() {
		return states;
	}

	public Set<IMatcher> getAllTransitions() {
		Set<IMatcher> result = new HashSet<>();

		for (AutomatonState s : states) {
			for (AutomatonTransition t : s.getOutgoingTransitions()) {
				IMatcher guard = t.getGuardCondition();
				if (guard != null)
					result.add(guard);
			}
		}

		return result;
	}

	public AutomatonStateSet move(IMatcher m) {
		Set<AutomatonState> targetStates = new HashSet<>();

		for (AutomatonState state : states) {
			AutomatonState t = state.move(m);
			if (t != null)
				targetStates.add(t);
		}

		return createEpsilonClosure(targetStates, null);
	}

	public boolean isEmpty() {
		return states.isEmpty();
	}

	public boolean containsFinalState() {
		for (AutomatonState state : states) {
			if (state.isFinalState())
				return true;
		}

		return false;
	}

	public static AutomatonStateSet createEpsilonClosure(Set<AutomatonState> states, AutomatonStateSet current) {
		if (current == null)
			current = new AutomatonStateSet();

		for (AutomatonState state : states) {
			if (current.states.contains(state))
				continue;
			current = createEpsilonClosure(state, current);
		}

		return current;
	}

	public static AutomatonStateSet createEpsilonClosure(AutomatonState state, AutomatonStateSet current) {
		if (current == null)
			current = new AutomatonStateSet();

		if (current.states.contains(state))
			return current;

		current.states.add(state);

		for (AutomatonTransition t : state.getOutgoingTransitions()) {
			if (current.states.contains(t))
				continue;

			if (t.getGuardCondition() == null) {
				current.states.addAll(AutomatonStateSet.createEpsilonClosure(t.getToState(), current).states);
			}
		}

		return current;
	}

	@Override
	public int hashCode() {
		return states.hashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof AutomatonStateSet) {
			return this.states.equals(((AutomatonStateSet) other).states);
		}

		return false;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();

		result.append("{ ");
		for (AutomatonState state : states) {
			result.append(state.toString() + " ");
		}
		result.append("}");

		return result.toString();
	}
}
