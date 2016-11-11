package com.kilic.kmeta.core.alls.atn;

import java.util.HashSet;
import java.util.Set;

import com.kilic.kmeta.core.alls.parser.IMutator;
import com.kilic.kmeta.core.alls.tn.IState.StateType;
import com.kilic.kmeta.core.alls.tn.TransitionNetworkBase;
import com.kilic.kmeta.core.util.CharSet;

public class ATN extends TransitionNetworkBase<Integer, IATNEdge, ATNState> {
	private final ATNState startState;
	private final ATNState finalState;
	private final Set<ATNCallEdge> callers;

	public ATN() {
		callers = new HashSet<>();
		startState = createState();
		finalState = createState();

		finalState.setType(StateType.FINAL);
	}

	public ATNState getStartState() {
		return startState;
	}

	public ATNState getFinalState() {
		return finalState;
	}

	public ATNState createState() {
		final ATNState newState = new ATNState(this);
		states.put(newState.getKey(), newState);
		return newState;
	}

	public ATNEpsilonEdge createEpsilonEdge(final ATNState from, final ATNState to) {
		return new ATNEpsilonEdge(from, to);
	}

	public ATNCharSetEdge createCharSetEdge(final ATNState from, final ATNState to, final CharSet charSet) {
		return new ATNCharSetEdge(from, to, charSet);
	}

	public void createEdgeFromString(final ATNState from, final ATNState to, final String string) {
		assert (string.length() > 0);

		ATNState currentState = from;
		for (int i = 0; i < string.length() - 1; i++) {
			ATNState nextState = createState();
			createCharSetEdge(currentState, nextState, new CharSet().addSingleton(string.charAt(i)));
			currentState = nextState;
		}

		final char lastChar = string.charAt(string.length() - 1);
		createCharSetEdge(currentState, to, new CharSet().addSingleton(lastChar));
	}

	public ATNCallEdge createCallEdge(final ATNState from, final ATNState to, final ATN atn) {
		return new ATNCallEdge(from, to, atn);
	}

	public ATNPredicateEdge createPredicateEdge(final ATNState from, final ATNState to) {
		return new ATNPredicateEdge(from, to);
	}

	public ATNMutatorEdge createMutatorEdge(final ATNState from, final ATNState to, final IMutator mutator) {
		return new ATNMutatorEdge(from, to, mutator);
	}

	public void addCaller(final ATNCallEdge edge) {
		callers.add(edge);
	}

	public Set<ATNCallEdge> getAllCallers() {
		return callers;
	}

	public String toGraphviz() {
		final StringBuilder result = new StringBuilder();

		result.append("digraph finite_state_machine {\n");
		result.append("  rankdir=S;\n");
		result.append("  size=\"10\"\n");
		result.append("node [shape = square];\n");
		result.append("S" + startState.getKey() + ";\n");
		result.append("node [shape = doublecircle];\n ");
		result.append("S" + finalState.getKey() + ";\n");
		result.append("node [shape = circle];");

		for (final ATNState state : states.values()) {
			for (final IATNEdge edge : state.getOut()) {
				result.append("S" + state.getKey() + " -> S" + edge.getTo().getKey() + " [ label = \"" + edge.getLabel()
						+ "\" ];\n");
			}
		}
		result.append("}\n");

		return result.toString();
	}
}
