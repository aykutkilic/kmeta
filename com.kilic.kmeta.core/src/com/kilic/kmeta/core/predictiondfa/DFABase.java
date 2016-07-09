package com.kilic.kmeta.core.predictiondfa;

import com.kilic.kmeta.core.tn.IEdge;
import com.kilic.kmeta.core.tn.IState;
import com.kilic.kmeta.core.tn.IState.StateType;
import com.kilic.kmeta.core.tn.TransitionNetworkBase;

public abstract class DFABase<K> extends TransitionNetworkBase<K> {
	IState<K> startState;
	IState<K> errorState;
	
	protected DFABase() {
		super();
	}
	
	public IState<K> getStartState() {
		return startState;
	}

	public void setStartState(IState<K> newStartState) {
		startState = newStartState;
	}
	
	public IState<K> getErrorState() {
		return errorState;
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();

		if (label != null)
			result.append(label + " : ");

		for (IState<K> state : states.values()) {
			if (state == startState)
				result.append("->");
			result.append(state.toString() + "\n");
			for (IEdge<K> edge : state.getOut() )
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

		for ( IState<K> s : states.values() ) {
			if(s.getType() == StateType.FINAL) 
				result.append("S" + s.getKey() + " ");
		}

		result.append(";\n");
		result.append("node [shape = circle];");
		for (IState<K> state : states.values()) {
			for ( IEdge<K> edge : state.getOut() ) {
				result.append("S" + state.getKey() + " -> S" + edge.getTo().getKey() + " [ label = \""
						+ edge.getLabel() + "\" ];\n");
			}
		}

		result.append("}\n");

		return result.toString();
	}
}
