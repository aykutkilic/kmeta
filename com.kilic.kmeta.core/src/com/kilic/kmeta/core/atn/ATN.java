package com.kilic.kmeta.core.atn;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.kilic.kmeta.core.util.CharSet;

public class ATN {
	ATNState startState;
	ATNState finalState;
	Set<ATNCallEdge> callers;
	
	Map<Integer, ATNState> states;
	
	String label = "";

	public ATN() {
		callers = new HashSet<>();
		states = new HashMap<>();
		
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
		ATNState newState = new ATNState(this);
		states.put(newState.stateIndex, newState);
		return newState;
	}
	
	public ATNEpsilonEdge createEpsilonEdge(ATNState from, ATNState to) {
		ATNEpsilonEdge edge = new ATNEpsilonEdge();
		connectEdge(from, to, edge);
		return edge;
	}

	public ATNCharSetEdge createCharSetEdge(ATNState from, ATNState to, CharSet charSet) {
		ATNCharSetEdge edge = new ATNCharSetEdge(charSet);
		connectEdge(from, to, edge);
		return edge;
	}

	public ATNStringEdge createStringEdge(ATNState from, ATNState to, String string) {
		ATNStringEdge edge = new ATNStringEdge(string);
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
		result.append("S" + startState.stateIndex + ";\n");
		result.append("node [shape = doublecircle];\n ");
		result.append("S" + finalState.stateIndex + ";\n");
		result.append("node [shape = circle];");
		
		for(ATNState state : states.values()) {
			for(IATNEdge edge : state.out) {
				result.append("S" + state.stateIndex + " -> S" + edge.getTo().stateIndex + " [ label = \""
						+ edge.getLabel() + "\" ];\n");
			}
		}
		result.append("}\n");

		return result.toString();
	}

	public void addCaller(ATNCallEdge edge) {
		callers.add(edge);
	}
	
	public Set<ATNCallEdge> getAllCallers() {
		return callers;
	}
}
