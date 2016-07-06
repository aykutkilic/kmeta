package com.kilic.kmeta.core.dfa;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.kilic.kmeta.core.atn.ATNConfigSet;
import com.kilic.kmeta.core.atn.IATNEdge;
import com.kilic.kmeta.core.util.CharSet;

public class DFA {
	String label;
	Map<ATNConfigSet, DFAState> states;
	Map<IATNEdge, DFAState> finalStates;
	
	DFAState startState;
	DFAState errorState;

	public DFA() {
		states = new HashMap<>();
		finalStates = new HashMap<>();
		
		errorState = new DFAState(this, null);
		errorState.setErrorState(true);
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
	
	public DFAState getErrorState() {
		return errorState;
	}

	public DFAState createFinalState(IATNEdge decisionEdge) {
		DFAState newState = new DFAState(this);
		newState.setFinal(decisionEdge);
		finalStates.put(decisionEdge, newState);
		return newState;
	}
	
	public DFAState createState(ATNConfigSet configSet) {
		DFAState newState = new DFAState(this, configSet);
		states.put(newState.configSet, newState);
		return newState;
	}

	public void createCharSetEdge(DFAState from, DFAState to, CharSet charSet) {
		DFACharSetEdge newEdge = new DFACharSetEdge(charSet);
		newEdge.from = from;
		newEdge.to = to;
		from.out.add(newEdge);
		to.in.add(newEdge);
	}
	
	public void createStringEdge(DFAState from, DFAState to, String string) {
		DFAStringEdge newEdge = new DFAStringEdge(string);
		newEdge.from = from;
		newEdge.to = to;
		from.out.add(newEdge);
		to.in.add(newEdge);
	}

	public DFAState getStartState() {
		return startState;
	}

	public void setStartState(DFAState newStartState) {
		startState = newStartState;
	}

	public DFAState getState(ATNConfigSet configSet) {
		if(states.containsKey(configSet))
			return states.get(configSet);
		
		return null;
	}
	
	public DFAState getFinalState(IATNEdge decisionEdge) {
		return finalStates.get(decisionEdge);
	}
	
	public Collection<DFAState> getFinalStates() {
		return finalStates.values();
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
		result.append("S" + startState.configSet.toString() + ";\n");
		result.append("node [shape = doublecircle];\n ");

		for (DFAState finalState : getFinalStates()) {
			result.append("S" + finalState.decisionEdge.toString() + " ");
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
