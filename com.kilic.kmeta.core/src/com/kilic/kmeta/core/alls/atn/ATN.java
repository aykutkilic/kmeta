package com.kilic.kmeta.core.alls.atn;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import com.kilic.kmeta.core.alls.nfa.NFA;
import com.kilic.kmeta.core.alls.nfa.NFAState;
import com.kilic.kmeta.core.alls.tn.StringEdgeBase;
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

	public ATNPredicateEdge createPredicateEdge(ATNState from, ATNState to) {
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
	public boolean canBeReducedToNFA() {
		for (ATNState state : this.states.values()) {
			for (IATNEdge edge : state.getOut()) {
				// this can be improved to look for looping/recursive calls etc.
				if (edge instanceof ATNCallEdge)
					return false;
			}
		}

		return true;
	}

	public NFA convertToNFA() {
		assert (canBeReducedToNFA());

		HashMap<HashSet<ATNState>, NFAState> dfaStates = new HashMap<>();

		NFA result = new NFA();
		Set<Set<ATNState>> closuresToProcess = new HashSet<>();
		closuresToProcess.add(getEpsilonClosure(getStartState()));

		while (closuresToProcess.size() > 0) {
			Set<ATNState> fromClosure = closuresToProcess.iterator().next();
			NFAState fromState = null;
			if (dfaStates.containsKey(fromClosure)) {
				fromState = dfaStates.get(fromClosure);
			} else
				fromState = result.createState();

			for (IATNEdge edge : getEdgesOfClosure(fromClosure)) {
				Set<ATNState> toClosure = moveClosure(fromClosure, edge);
				NFAState toState = null;
				if (dfaStates.containsKey(toClosure)) {
					toState = dfaStates.get(toClosure);
				} else
					toState = result.createState();

				if (edge instanceof ATNStringEdge) {
					for( char c : ((ATNStringEdge) edge).getString().toCharArray() ) {
						
					}
				} else if (edge instanceof ATNCharSetEdge) {

				}

			}
		}
	}

	Set<ATNState> getEpsilonClosure(ATNState state) {
		Set<ATNState> result = new HashSet<>();
		result.add(state);

		for (IATNEdge out : state.getOut()) {
			if (out instanceof ATNEpsilonEdge)
				result.addAll(getEpsilonClosure(out.getTo()));
		}

		return result;
	}

	Set<IATNEdge> getEdgesOfClosure(Set<ATNState> epsilonClosure) {
		Set<IATNEdge> result = new HashSet<>();

		for (ATNState state : epsilonClosure) {
			for (IATNEdge out : state.getOut()) {
				if (out instanceof ATNStringEdge || out instanceof ATNCharSetEdge) {
					result.add(out);
				}
			}
		}

		return result;
	}

	Set<ATNState> moveClosure(Set<ATNState> epsilonClosure, IATNEdge edge) {
		Set<ATNState> result = new HashSet<>();

		for (ATNState state : epsilonClosure) {
			ATNState toState = moveState(state, edge);
			if (toState != null)
				result.add(toState);
		}

		return result;
	}

	ATNState moveState(ATNState state, IATNEdge edge) {
		for (IATNEdge out : state.getOut()) {
			if (out.equals(edge))
				return out.getTo();
		}

		return null;
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

		for (ATNState state : states.values()) {
			for (IATNEdge edge : state.getOut()) {
				result.append("S" + state.getKey() + " -> S" + edge.getTo().getKey() + " [ label = \"" + edge.getLabel()
						+ "\" ];\n");
			}
		}
		result.append("}\n");

		return result.toString();
	}
}
