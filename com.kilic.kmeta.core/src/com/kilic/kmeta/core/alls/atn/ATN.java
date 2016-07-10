package com.kilic.kmeta.core.alls.atn;

import java.util.HashSet;
import java.util.Set;

import com.kilic.kmeta.core.alls.tn.IEdge;
import com.kilic.kmeta.core.alls.tn.IState;
import com.kilic.kmeta.core.alls.tn.TransitionNetworkBase;
import com.kilic.kmeta.core.util.CharSet;

public class ATN extends TransitionNetworkBase<Integer, IATNEdge, ATNState> {
	ATNState startState;
	ATNState finalState;
	Set<ATNCallEdge> callers;
	
	public ATN() {
		callers = new HashSet<>();
		startState = createState();
		finalState = createState();
	}
	
	public ATNState getStartState() {
		return startState;
	}
	
	public ATNState getFinalState() {
		return finalState;
	}

	public ATNState createState() {
		ATNState newState = new ATNState(this);
		states.put(newState.getKey(), newState);
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
	
	public void addCaller(ATNCallEdge edge) {
		callers.add(edge);
	}
	
	public Set<ATNCallEdge> getAllCallers() {
		return callers;
	}

	// returns true if the ATN only contains token and epsilon edges..
	public boolean canBeReducedToDFA() {
		return false;
	}
	
	public String toGraphviz() {
		StringBuilder result = new StringBuilder();

		result.append("digraph finite_state_machine {\n");
		result.append("  rankdir=S;\n");
		result.append("  size=\"8,5\"\n");
		result.append("node [shape = square];\n");
		result.append("S" + startState.getKey() + ";\n");
		result.append("node [shape = doublecircle];\n ");
		result.append("S" + finalState.getKey() + ";\n");
		result.append("node [shape = circle];");
		
		for(ATNState state : states.values()) {
			for(IATNEdge edge : state.getOut()) {
				result.append("S" + state.getKey() + " -> S" + edge.getTo().getKey() + " [ label = \""
						+ edge.getLabel() + "\" ];\n");
			}
		}
		result.append("}\n");

		return result.toString();
	}
}
