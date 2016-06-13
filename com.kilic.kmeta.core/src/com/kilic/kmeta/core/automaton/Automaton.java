package com.kilic.kmeta.core.automaton;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.kilic.kmeta.core.stream.IStream;

public class Automaton {
	Map<Integer, AutomatonState> states;
	AutomatonState startState;

	AutomatonState currentState;

	public Automaton() {
		states = new HashMap<>();
	}

	public AutomatonState createState() {
		AutomatonState newState = new AutomatonState();
		states.put(newState.stateIndex, newState);
		return newState;
	}

	public AutomatonState getStartState() {
		return startState;
	}

	public void setStartState(AutomatonState newStartState) {
		startState = newStartState;
	}

	public AutomatonState findStateByAttachedObject(Object o) {
		for (AutomatonState state : states.values()) {
			if (state.attachedObject == o)
				return state;
		}

		return null;
	}

	public IAutomatonTransition findTransitionByAttachedObject(Object o) {
		for (AutomatonState state : states.values()) {
			for (IAutomatonTransition trans : state.getOutgoingTransitions()) {
				if (trans.getAttachedObject() == o)
					return trans;
			}
		}

		return null;
	}

	public void removeState(AutomatonState state) {
		states.remove(state);
	}

	public void createMatcherTransition(AutomatonState from, AutomatonState to, IMatcher matcher) {
		MatcherTransition t = new MatcherTransition(from, to, matcher);

		from.addOutgoingTransition(t);
		to.addIncomingTransition(t);
	}
	
	public void createEpsilonTransition(AutomatonState from, AutomatonState to) {
		// ignore epsilon transitions to self.
		if (from == to)
			return;

		EpsilonTransition t = new EpsilonTransition(from, to);

		from.addOutgoingTransition(t);
		to.addIncomingTransition(t);
	}
	
	private void createEquivalentTransition(AutomatonState from, AutomatonState to, IAutomatonTransition t) {
		IAutomatonTransition newTransition = null;
		
		if(t instanceof MatcherTransition) {
			newTransition = new MatcherTransition(from, to, ((MatcherTransition) t).getMatcher());
		} else if( t instanceof EpsilonTransition) {
			newTransition = new EpsilonTransition(from,to);
		}
		
		assert(newTransition!=null);
		
		from.addOutgoingTransition(newTransition);
		to.addIncomingTransition(newTransition);
	}

	public Set<AutomatonState> getFinalStates() {
		Set<AutomatonState> result = new HashSet<>();

		for (AutomatonState state : states.values()) {
			if (state.isFinalState)
				result.add(state);
		}

		return result;
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

			for (IAutomatonTransition t : currentNFAClosure.getAllTransitions()) {
				AutomatonStateSet newNFAClosure = currentNFAClosure.move(t);
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

		for (AutomatonState state : states.values()) {
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
		result.append("S"+ startState.stateIndex + ";\n");
		result.append("node [shape = doublecircle];\n ");
		
		for (AutomatonState finalState : getFinalStates()) {
			result.append("S" + finalState.stateIndex + " ");
		}
		
		result.append(";\n");
		result.append("node [shape = circle];");
		for (AutomatonState state : states.values()) {
			for (IAutomatonTransition trans : state.out) {
				result.append("S" + state.stateIndex + " -> S" + trans.getToState().stateIndex + " [ label = \"" 
						+ trans.getLabel() + "\" ];\n");
			}
		}

		result.append("}\n");

		return result.toString();
	}
}
