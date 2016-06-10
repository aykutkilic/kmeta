package com.kilic.kmeta.core.dfa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.kilic.kmeta.core.stream.IStream;

public class Automaton implements IMatcher {
	Set<AutomatonState> states;
	AutomatonState startState;

	public Automaton() {
		states = new HashSet<>();
	}

	public AutomatonState createState() {
		AutomatonState newState = new AutomatonState();
		states.add(newState);
		return newState;
	}

	public AutomatonState getStartState() {
		return startState;
	}

	public void setStartState(AutomatonState newStartState) {
		startState = newStartState;
	}

	public AutomatonState findStateByAttachedObject(Object o) {
		for (AutomatonState state : states) {
			if (state.attachedObject == o)
				return state;
		}

		return null;
	}

	public AutomatonTransition findTransitionByAttachedObject(Object o) {
		for (AutomatonState state : states) {
			for (AutomatonTransition trans : state.getOutgoingTransitions()) {
				if (trans.getAttachedObject() == o)
					return trans;
			}
		}

		return null;
	}

	public void removeState(AutomatonState state) {
		states.remove(state);
	}

	// null guard condition means epsilon.
	public void createTransition(AutomatonState from, AutomatonState to, IMatcher condition) {
		AutomatonTransition t = new AutomatonTransition(from, to, condition);

		from.addOutgoingTransition(t);
		to.addIncomingTransition(t);
	}

	@Override
	public boolean match(IStream stream) {
		AutomatonState current = startState;

		while (!current.isFinalState()) {
			AutomatonTransition[] outgoing = current.getOutgoingTransitions();
			List<AutomatonState> epsilonStates = new ArrayList<AutomatonState>();

			for (AutomatonTransition t : outgoing) {
				IMatcher m = t.getGuardCondition();
				if (m == null) {
					epsilonStates.add(t.getToState());
				}
			}
		}

		return false;
	}

	public Automaton converteNFAToDFA() {
		Automaton result = new Automaton();

		Map<AutomatonStateSet, AutomatonState> nfaClosureToDfaState = new HashMap<>();

		if (startState == null)
			return result;

		AutomatonStateSet startNFAClosure = AutomatonStateSet.createEpsilonClosure(startState, null);
		AutomatonState startDFAState = result.createState();
		nfaClosureToDfaState.put(startNFAClosure, startDFAState);

		Set<AutomatonStateSet> incompleteClosures = new HashSet<>();
		incompleteClosures.add(startNFAClosure);

		while (!incompleteClosures.isEmpty()) {
			AutomatonStateSet currentNFAClosure = incompleteClosures.iterator().next();
			AutomatonState currentDFAState = nfaClosureToDfaState.get(currentNFAClosure);

			for (IMatcher m : currentNFAClosure.getAllTransitions()) {
				AutomatonStateSet newNFAClosure = currentNFAClosure.move(m);
				if (newNFAClosure.isEmpty())
					continue;

				if (!nfaClosureToDfaState.containsKey(newNFAClosure)) {
					AutomatonState newDFAState = result.createState();
					nfaClosureToDfaState.put(newNFAClosure, newDFAState);
					result.createTransition(currentDFAState, newDFAState, m);
					incompleteClosures.add(newNFAClosure);
				} else {
					AutomatonState toState = nfaClosureToDfaState.get(newNFAClosure);
					result.createTransition(currentDFAState, toState, m);
				}
			}

			incompleteClosures.remove(currentNFAClosure);
		}

		return result;
	}
}
