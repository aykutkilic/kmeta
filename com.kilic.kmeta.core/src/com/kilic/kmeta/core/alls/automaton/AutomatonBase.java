package com.kilic.kmeta.core.alls.automaton;

import com.kilic.kmeta.core.alls.tn.IEdge;
import com.kilic.kmeta.core.alls.tn.IState;
import com.kilic.kmeta.core.alls.tn.IState.StateType;
import com.kilic.kmeta.core.alls.tn.TransitionNetworkBase;

public abstract class AutomatonBase<K,E extends IEdge<S>, S extends IState<K,E>> extends TransitionNetworkBase<K,E,S> {
	protected S startState;
	protected S errorState;
	
	protected AutomatonBase() {
		super();
	}
	
	public S getStartState() {
		return startState;
	}

	public void setStartState(final S newStartState) {
		startState = newStartState;
	}
	
	public S getErrorState() {
		return errorState;
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();

		if (label != null)
			result.append(label + " : ");

		for (S state : states.values()) {
			if (state == startState)
				result.append("->");
			result.append(state.toString() + "\n");
			for (E edge : state.getOut() )
				result.append("    " + edge.toString() + "\n");
		}

		return result.toString();
	}

	public String toGraphviz() {
		final StringBuilder result = new StringBuilder();

		result.append("digraph finite_state_machine {\n");
		result.append("  rankdir=S;\n");
		result.append("  size=\"8,5\"\n");
		result.append("node [shape = square];\n");
		if(startState!=null)
			result.append("S" + startState.getLabel() + ";\n"); 
		result.append("node [shape = doublecircle];\n ");

		boolean hasFinalState = false;
		for ( S s : states.values() ) {
			if(s.getType() == StateType.FINAL) { 
				result.append("S" + s.getLabel() + " ");
				hasFinalState = true;
			}
		}

		if(hasFinalState)
			result.append(";\n");
		
		result.append("node [shape = circle];");
		for (S state : states.values()) {
			for ( E edge : state.getOut() ) {
				result.append("S" + state.getLabel() + " -> S" + edge.getTo().getLabel() + " [ label = \""
						+ edge.getLabel() + "\" ];\n");
			}
		}

		result.append("}\n");

		return result.toString();
	}
}
