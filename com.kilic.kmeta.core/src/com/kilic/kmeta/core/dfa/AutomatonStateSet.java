package com.kilic.kmeta.core.dfa;

import java.util.HashSet;
import java.util.Set;

public class AutomatonStateSet {
	Set<AutomatonState> states;

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
}
