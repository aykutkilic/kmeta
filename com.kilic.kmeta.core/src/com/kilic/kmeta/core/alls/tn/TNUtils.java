package com.kilic.kmeta.core.alls.tn;

import java.util.Collection;

import com.kilic.kmeta.core.alls.tn.IState.StateType;

public class TNUtils {
	public static String toGraphviz(final Collection<TransitionNetworkBase<?, ?, ?>> tns) {
		final StringBuilder result = new StringBuilder();

		result.append("digraph finite_state_machine {\n");
		result.append("  rankdir=S;\n");
		result.append("  size=\"8,5\"\n");

		result.append("node [shape = square];\n");
		boolean hasStartState = false;
		for (final TransitionNetworkBase<?, ?, ?> tn : tns) {
			final IState<?, ?> startState = tn.getStartState();
			if (startState != null) {
				result.append(" S" + startState.getKey().toString());
				hasStartState = true;
			}
		}
		if (hasStartState)
			result.append(";\n");

		result.append("node [shape = doublecircle];\n ");
		boolean hasFinalState = false;
		for (final TransitionNetworkBase<?, ?, ?> tn : tns) {
			for (final IState<?, ?> state : tn.getStates()) {
				if (state.getType() == StateType.FINAL) {
					result.append(" S" + state.getKey().toString());
					hasFinalState = true;
				}
			}
		}
		if (hasFinalState)
			result.append(";\n");

		result.append("node [shape = circle];");

		for (final TransitionNetworkBase<?, ?, ?> tn : tns) {
			for (final IState<?, ?> state : tn.getStates()) {
				for (final IEdge<?> edge : state.getOut()) {
					result.append("S" + state.getKey() + " -> S" + edge.getTo().getKey() + " [ label = \""
							+ edge.getLabel() + "\" ];\n");
				}
			}
		}
		result.append("}\n");
		return result.toString();
	}
}
