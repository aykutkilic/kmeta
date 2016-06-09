package com.kilic.kmeta.core.dfa;

import java.util.HashSet;
import java.util.Set;

public class DFAStateSet {
	Set<DFAState> states;

	public Set<DFAState> getStates() {
		return states;
	}

	public Set<IMatcher> getAllTransitions() {
		Set<IMatcher> result = new HashSet<>();

		for (DFAState s : states) {
			for (DFATransition t : s.getOutgoingTransitions()) {
				IMatcher guard = t.getGuardCondition();
				if (guard != null)
					result.add(guard);
			}
		}

		return result;
	}

	public static DFAStateSet createEpsilonClosure(DFAState state, DFAStateSet current) {
		if (current == null)
			current = new DFAStateSet();

		current.states.add(state);

		for (DFATransition t : state.getOutgoingTransitions()) {
			if (current.states.contains(t))
				continue;

			if (t.getGuardCondition() == null) {
				current.states.addAll(DFAStateSet.createEpsilonClosure(t.getToState(), current).states);
			}
		}

		return current;
	}
}
