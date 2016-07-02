package com.kilic.kmeta.core.atn;

import com.kilic.kmeta.core.util.CharSet;

public class ATN {
	ATN container;

	IATNState startState;
	IATNState finalState;

	String label = "";

	public ATN(ATN container) {
		this.container = container;
		
		startState = createRegularState();
		finalState = createFinalState();
	}
	
	public void setLabel(String label) {
		this.label = label;
	} 

	public String getLabel() {
		return this.label;
	}
	
	public IATNState getStartState() {
		return startState;
	}
	
	public IATNState getFinalState() {
		return finalState;
	}

	public RegularATNState createRegularState() {
		return new RegularATNState(false);
	}
	
	public RegularATNState createFinalState() {
		return new RegularATNState(true);
	}

	public DecisionState createDecisionState() {
		return new DecisionState();
	}
	
	public EpsilonEdge createEpsilonEdge(IATNState from, IATNState to) {
		EpsilonEdge edge = new EpsilonEdge();
		connectEdge(from, to, edge);
		return edge;
	}

	public CharSetEdge createCharSetEdge(IATNState from, IATNState to, CharSet charSet) {
		CharSetEdge edge = new CharSetEdge(charSet);
		connectEdge(from, to, edge);
		return edge;
	}

	public StringEdge createStringEdge(IATNState from, IATNState to, String string) {
		StringEdge edge = new StringEdge(string);
		connectEdge(from, to, edge);
		return edge;
	}

	public ATNCallEdge createCallEdge(IATNState from, IATNState to, ATN atn) {
		ATNCallEdge edge = new ATNCallEdge(atn);
		connectEdge(from, to, edge);
		return edge;
	}
	
	public ATNPredicateEdge createPredicateEdge(IATNState from, IATNState to ) {
		ATNPredicateEdge edge = new ATNPredicateEdge();
		connectEdge(from, to, edge);
		return edge;
	}
	
	public ATNMutatorEdge createMutatorEdge(IATNState from, IATNState to) {
		ATNMutatorEdge edge = new ATNMutatorEdge();
		connectEdge(from, to, edge);
		return edge;
	}

	void connectEdge(IATNState from, IATNState to, ATNEdgeBase edge) {
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
