 package com.kilic.kmeta.core.atn;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ATN {
	ATN container;
	List<ATN> productions;

	ATNState startState;
	ATNState finalState;

	String label = "";

	public ATN(ATN container) { 
		this.container = container;
		startState = new ATNState();
		finalState = new ATNState();
	}

	public ATN createProduction() {
		ATN result = new ATN(this);

		return result;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getLabel() {
		return this.label;
	}

	public Set<ATNConfig> getMovedConfigSet() {
	}
	
	public Set<ATNConfig> getClosure(ATNConfig config) {
		Set<ATNConfig> result = new HashSet<>();
		
		return result;
	}
	
	public String toGraphviz() {
		StringBuilder result = new StringBuilder();

		result.append("digraph finite_state_machine {\n");
		result.append("  rankdir=S;\n");
		result.append("  size=\"8,5\"\n");
		result.append("node [shape = square];\n");
		result.append("S" + startState.stateIndex + ";\n");
		result.append("node [shape = doublecircle];\n ");
		result.append("S" + finalState.stateIndex + " ");

		result.append(";\n");
		result.append("node [shape = circle];");
		for (ATN production : productions) {
			result.append(production.toGraphviz());

			/*
			 * for (IAutomatonTransition trans : state.out) { result.append("S"
			 * + state.stateIndex + " -> S" + trans.getToState().stateIndex +
			 * " [ label = \"" + trans.getLabel() + "\" ];\n"); }
			 */
		}

		result.append("}\n");

		return result.toString();
	}
}
