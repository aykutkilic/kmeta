package com.kilic.kmeta.core.atn;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.kilic.kmeta.core.util.CharSet;

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

	public ATNCallEdge createATNCallEdge(ATNState from, ATNState to, ATN atn) {
		ATNCallEdge edge = new ATNCallEdge(atn);
		connectEdge(from, to, edge);
		return edge;
	}

	void connectEdge(ATNState from, ATNState to, ATNEdgeBase edge) {
		edge.from = from;
		edge.to = to;

		from.out.add(edge);
		to.in.add(edge);
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
