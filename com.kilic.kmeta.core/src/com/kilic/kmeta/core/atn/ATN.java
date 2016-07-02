package com.kilic.kmeta.core.atn;

import com.kilic.kmeta.core.util.CharSet;

public class ATN {
	ATN container;

	ATNState startState;
	ATNState finalState;

	String label = "";

	public ATN(ATN container) {
		this.container = container;
		
		startState = createState();
		finalState = createState();
	}
	
	public void setLabel(String label) {
		this.label = label;
	} 

	public String getLabel() {
		return this.label;
	}
	
	public ATNState getStartState() {
		return startState;
	}
	
	public ATNState getFinalState() {
		return finalState;
	}

	public ATNState createState() {
		return new ATNState();
	}
	
	public EpsilonEdge createEpsilonEdge(ATNState from, ATNState to) {
		EpsilonEdge edge = new EpsilonEdge();
		connectEdge(from, to, edge);
		return edge;
	}

	public CharSetEdge createCharSetEdge(ATNState from, ATNState to, CharSet charSet) {
		CharSetEdge edge = new CharSetEdge(charSet);
		connectEdge(from, to, edge);
		return edge;
	}

	public StringEdge createStringEdge(ATNState from, ATNState to, String string) {
		StringEdge edge = new StringEdge(string);
		connectEdge(from, to, edge);
		return edge;
	}

	public ATNCallEdge createCallEdge(ATNState from, ATNState to, ATN atn) {
		ATNCallEdge edge = new ATNCallEdge(atn);
		connectEdge(from, to, edge);
		return edge;
	}
	
	public ATNPredicateEdge createPredicateEdge(ATNState from, ATNState to ) {
		ATNPredicateEdge edge = new ATNPredicateEdge();
		connectEdge(from, to, edge);
		return edge;
	}
	
	public ATNMutatorEdge createMutatorEdge(ATNState from, ATNState to) {
		ATNMutatorEdge edge = new ATNMutatorEdge();
		connectEdge(from, to, edge);
		return edge;
	}

	void connectEdge(ATNState from, ATNState to, ATNEdgeBase edge) {
		edge.from = from;
		edge.to = to;
		from.addOut(edge);
		to.addIn(edge);
	}

	public String toGraphviz() {
		StringBuilder result = new StringBuilder();

		result.append("digraph finite_state_machine {\n");
		result.append("  rankdir=S;\n");
		result.append("  size=\"8,5\"\n");
		result.append("node [shape = square];\n");
		result.append("S" + startState.getStateIndex() + ";\n");
		result.append("node [shape = doublecircle];\n ");
		result.append("S" + finalState.getStateIndex() + " ");

		result.append(";\n");
		result.append("node [shape = circle];");
		result.append("}\n");

		return result.toString();
	}
}
