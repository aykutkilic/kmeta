package com.kilic.kmeta.core.automaton;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.kilic.kmeta.core.stream.IStream;

public class Automaton implements IMatcher {
	Set<AutomatonState> states;
	AutomatonState startState;

	AutomatonState currentState;

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
		// ignore epsilon transitions to self.
		if (from == to && condition == null)
			return;

		AutomatonTransition t = new AutomatonTransition(from, to, condition);

		from.addOutgoingTransition(t);
		to.addIncomingTransition(t);
	}

	@Override
	public boolean match(IStream stream) {
		AutomatonState current = startState;

		boolean hasMatch;
		do {
			hasMatch = false;
			Set<AutomatonTransition> outgoing = current.getOutgoingTransitions();

			for (AutomatonTransition t : outgoing) {
				IMatcher m = t.getGuardCondition();
				if (m.match(stream)) {
					current = t.getToState();
					hasMatch = true;
					break;
				}
			}
		} while (hasMatch);

		return current.isFinalState();
	}

	public Automaton convertNFAToDFA() {
		Automaton result = new Automaton();

		Map<AutomatonStateSet, AutomatonState> nfaClosureToDfaState = new HashMap<>();

		if (startState == null)
			return result;

		AutomatonStateSet startNFAClosure = AutomatonStateSet.createEpsilonClosure(startState, null);
		AutomatonState startDFAState = result.createState();
		result.setStartState(startDFAState);
		startDFAState.setFinal(startNFAClosure.containsFinalState());

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

				AutomatonState toState;

				if (!nfaClosureToDfaState.containsKey(newNFAClosure)) {
					AutomatonState newDFAState = result.createState();
					newDFAState.setFinal(newNFAClosure.containsFinalState());

					nfaClosureToDfaState.put(newNFAClosure, newDFAState);
					incompleteClosures.add(newNFAClosure);

					toState = newDFAState;
				} else {
					toState = nfaClosureToDfaState.get(newNFAClosure);
				}

				// if this transition is not already added:
				if (currentDFAState.move(m) != toState)
					result.createTransition(currentDFAState, toState, m);
			}

			incompleteClosures.remove(currentNFAClosure);
		}

		return result;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();

		for (AutomatonState state : states) {
			if (state == startState)
				result.append("->");
			result.append(state.toString() + "\n");
			for (AutomatonTransition transition : state.getOutgoingTransitions())
				result.append("    " + transition.toString() + "\n");
		}

		return result.toString();
	}

	public Set<AutomatonState> getFinalStates() {
		Set<AutomatonState> result = new HashSet<>();

		for (AutomatonState state : states) {
			if (state.isFinalState)
				result.add(state);
		}

		return result;
	}

	public String toGraphviz() {
		StringBuilder result = new StringBuilder();

		result.append("digraph finite_state_machine {\n");
		result.append("  rankdir=S;\n");
		result.append("  size=\"8,5\"\n");
		result.append("node [shape = doublecircle]; ");
		for (AutomatonState finalState : getFinalStates()) {
			result.append("S" + finalState.stateIndex + " ");
		}
		result.append(";");
		result.append("node [shape = circle];");
		for (AutomatonState state : states) {
			for (AutomatonTransition trans : state.out) {
				IMatcher m = trans.getGuardCondition();
				String label = m != null ? m.toString() : "<e>";
				result.append("S" + state.stateIndex + " -> S" + trans.getToState().stateIndex + " [ label = \"" + label
						+ "\" ];\n");
			}
		}

		result.append("}\n");

		return result.toString();
	}
}
