package com.kilic.kmeta.core.dfa;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.kilic.kmeta.core.util.CharSet;

public class DFA {
	String label;
	Map<Integer, DFAState> states;
	DFAState startState;

	DFAState currentState;

	public DFA() {
		states = new HashMap<>();
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public DFAState createState() {
		DFAState newState = new DFAState(this);
		states.put(newState.stateIndex, newState);
		return newState;
	}

	public DFAState getStartState() {
		return startState;
	}

	public void setStartState(DFAState newStartState) {
		startState = newStartState;
	}

	public DFAState findStateByAttachedObject(Object o) {
		for (DFAState state : states.values()) {
			if (state.attachedObject == o)
				return state;
		}

		return null;
	}

	public void removeState(DFAState state) {
		states.remove(state);
	}

	public void createMatcherTransition(DFAState from, DFAState to, IMatcher matcher) {
		// string matchers are substitutes with charset matchers until
		// intersection analysis is completed
		// sequence of singleton charsets can be shrinked afterwards for
		// performance improvements
		if (matcher instanceof StringMatcher) {
			String string = ((StringMatcher) matcher).getString();
			DFAState newFrom = from;
			for (int i = 0; i < string.length(); i++) {
				DFAState newTo = i == string.length() - 1 ? to : createState();

				CharSet charSet = new CharSet();
				charSet.addSingleton(string.charAt(i));
				createMatcherTransition(newFrom, newTo, new CharSetMatcher(charSet));

				newFrom = newTo;
			}
		} else {
			MatcherTransition t = new MatcherTransition(from, to, matcher);

			from.addOutgoingTransition(t);
			to.addIncomingTransition(t);
		}
	}

	public void createEpsilonTransition(DFAState from, DFAState to) {
		// ignore epsilon transitions to self.
		if (from == to)
			return;

		EpsilonTransition t = new EpsilonTransition(from, to);

		from.addOutgoingTransition(t);
		to.addIncomingTransition(t);
	}

	public void createCallTransition(DFAState from, DFAState to, DFA callee) {
		CallAutomatonTransition t = new CallAutomatonTransition(from, to, callee);

		from.addOutgoingTransition(t);
		to.addIncomingTransition(t);
	}

	public void createEquivalentTransition(DFAState from, DFAState to, IAutomatonTransition t) {
		IAutomatonTransition newTransition = null;

		if (t instanceof MatcherTransition) {
			newTransition = new MatcherTransition(from, to, ((MatcherTransition) t).getMatcher());
		} else if (t instanceof EpsilonTransition) {
			newTransition = new EpsilonTransition(from, to);
		} else if (t instanceof CallAutomatonTransition) {
			newTransition = new CallAutomatonTransition(from, to, ((CallAutomatonTransition) t).getAutomaton());
		}

		assert (newTransition != null);

		from.addOutgoingTransition(newTransition);
		to.addIncomingTransition(newTransition);
	}

	public Set<DFAState> getFinalStates() {
		Set<DFAState> result = new HashSet<>();

		for (DFAState state : states.values()) {
			if (state.isFinalState)
				result.add(state);
		}

		return result;
	}

	public DFA convertNFAToDFA() {
		DFA result = new DFA();

		Map<AutomatonStateSet, DFAState> nfaClosureToDfaState = new HashMap<>();

		if (startState == null)
			return result;

		AutomatonStateSet startNFAClosure = AutomatonStateSet.createEpsilonClosure(startState, null);
		DFAState startDFAState = result.createState();
		result.setStartState(startDFAState);
		startDFAState.setFinal(startNFAClosure.containsFinalState());

		nfaClosureToDfaState.put(startNFAClosure, startDFAState);

		Set<AutomatonStateSet> incompleteClosures = new HashSet<>();
		incompleteClosures.add(startNFAClosure);

		while (!incompleteClosures.isEmpty()) {
			AutomatonStateSet currentNFAClosure = incompleteClosures.iterator().next();
			DFAState currentDFAState = nfaClosureToDfaState.get(currentNFAClosure);

			for (IAutomatonTransition t : currentNFAClosure.getAllTransitions()) {
				AutomatonStateSet newNFAClosure = currentNFAClosure.move(t);
				if (newNFAClosure.isEmpty())
					continue;

				DFAState toState;

				if (!nfaClosureToDfaState.containsKey(newNFAClosure)) {
					DFAState newDFAState = result.createState();
					newDFAState.setFinal(newNFAClosure.containsFinalState());

					nfaClosureToDfaState.put(newNFAClosure, newDFAState);
					incompleteClosures.add(newNFAClosure);

					toState = newDFAState;
				} else {
					toState = nfaClosureToDfaState.get(newNFAClosure);
				}

				// if this transition is not already added:
				if (currentDFAState.move(t) != toState)
					result.createEquivalentTransition(currentDFAState, toState, t);
			}

			incompleteClosures.remove(currentNFAClosure);
		}

		return result;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();

		if (label != null)
			result.append(label + " : ");

		for (DFAState state : states.values()) {
			if (state == startState)
				result.append("->");
			result.append(state.toString() + "\n");
			for (IAutomatonTransition transition : state.getOutgoingTransitions())
				result.append("    " + transition.toString() + "\n");
		}

		return result.toString();
	}

	public String toGraphviz() {
		StringBuilder result = new StringBuilder();

		result.append("digraph finite_state_machine {\n");
		result.append("  rankdir=S;\n");
		result.append("  size=\"8,5\"\n");
		result.append("node [shape = square];\n");
		result.append("S" + startState.stateIndex + ";\n");
		result.append("node [shape = doublecircle];\n ");

		for (DFAState finalState : getFinalStates()) {
			result.append("S" + finalState.stateIndex + " ");
		}

		result.append(";\n");
		result.append("node [shape = circle];");
		for (DFAState state : states.values()) {
			for (IAutomatonTransition trans : state.out) {
				result.append("S" + state.stateIndex + " -> S" + trans.getToState().stateIndex + " [ label = \""
						+ trans.getLabel() + "\" ];\n");
			}
		}

		result.append("}\n");

		return result.toString();
	}
}
