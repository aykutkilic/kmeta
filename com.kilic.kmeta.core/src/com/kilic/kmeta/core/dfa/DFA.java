package com.kilic.kmeta.core.dfa;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.kilic.kmeta.core.stream.IStream;

public class DFA implements IMatcher {
	Set<DFAState> states;
	DFAState startState;

	public DFA() {
		states = new HashSet<>();
	}

	public DFAState createState() {
		DFAState newState = new DFAState();
		states.add(newState);
		return newState;
	}

	public DFAState getStartState() {
		return startState;
	}

	public void setStartState(DFAState newStartState) {
		startState = newStartState;
	}

	public void removeState(DFAState state) {
		states.remove(state);
	}

	// null guard condition means epsilon.
	public void createTransition(DFAState from, DFAState to, IMatcher condition) {
		DFATransition t = new DFATransition(from, to, condition);

		from.addOutgoingTransition(t);
		to.addIncomingTransition(t);
	}

	@Override
	public boolean match(IStream stream) {
		DFAState current = startState;

		while (!current.isFinalState()) {
			DFATransition[] outgoing = current.getOutgoingTransitions();
			List<DFAState> epsilonStates = new ArrayList<DFAState>();

			for (DFATransition t : outgoing) {
				IMatcher m = t.getGuardCondition();
				if (m == null) {
					epsilonStates.add(t.getToState());
				}
			}
		}

		return false;
	}

	public DFA converteNFAToDFA() {
		DFA result = new DFA();
		Set<DFAState> completedStates = new HashSet<>();

		DFAState currentState = startState;
		DFAStateSet eclosure = DFAStateSet.createEpsilonClosure(currentState, null);

		return result;
	}
}
