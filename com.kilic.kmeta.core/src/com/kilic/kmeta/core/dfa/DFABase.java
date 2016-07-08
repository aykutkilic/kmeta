package com.kilic.kmeta.core.dfa;

import java.util.HashMap;
import java.util.Map;

public abstract class DFABase<SK> implements IDFA {
	String label;
	
	IDFAState<SK> startState;
	IDFAState<SK> errorState;
	
	Map<SK, IDFAState<SK>> states;
	
	protected DFABase() {
		states = new HashMap<>();
	}
	
	public void setLabel(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
	
	public IDFAState getStartState() {
		return startState;
	}

	public void setStartState(IDFAState newStartState) {
		startState = newStartState;
	}
	
	public IDFAState getErrorState() {
		return errorState;
	}
	
	public IDFAState getState(SK key) {
		return states.get(key);
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();

		if (label != null)
			result.append(label + " : ");

		for (IDFAState<SK> state : states.values()) {
			if (state == startState)
				result.append("->");
			result.append(state.toString() + "\n");
			for (IDFAEdge<SK> edge : state.getOut() )
				result.append("    " + edge.toString() + "\n");
		}

		return result.toString();
	}

	public String toGraphviz() {
		StringBuilder result = new StringBuilder();

		result.append("digraph finite_state_machine {\n");
		result.append("  rankdir=S;\n");
		result.append("  size=\"8,5\"\n");
		result.append("node [shape = square];\n");
		result.append("S" + startState.getKey().toString() + ";\n"); 
		result.append("node [shape = doublecircle];\n ");

		for ( IDFAState<SK> s : states.values() ) {
			if(s.isFinal()) 
				result.append("S" + s.getKey() + " ");
		}

		result.append(";\n");
		result.append("node [shape = circle];");
		for (IDFAState<SK> state : states.values()) {
			for ( IDFAEdge<SK> edge : state.getOut() ) {
				result.append("S" + state.getKey() + " -> S" + edge.getTo().getKey() + " [ label = \""
						+ edge.getLabel() + "\" ];\n");
			}
		}

		result.append("}\n");

		return result.toString();
	}
}
