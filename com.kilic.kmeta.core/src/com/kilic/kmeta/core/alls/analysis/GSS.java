package com.kilic.kmeta.core.alls.analysis;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

// Graph Structured Stack
// Used for step locked execution of ATNs or DFA.
public class GSS {
	public static final int ANY_STACK = -1;

	private Map<Integer, GSSNode> top;
	private Set<GSSNode> nodes;

	public GSS() {
		top = new HashMap<>();
		nodes = new HashSet<>();
	}

	public GSS(final GSS other) {
		top = new HashMap<>(other.top);
		nodes = new HashSet<>(other.nodes);
	}

	public Collection<GSSNode> getStackTops() {
		return top.values();
	}

	public void merge(final Integer state) {
		if (state == ANY_STACK) {
			nodes.clear();
			top.clear();
		}

		if (top.containsKey(state))
			return;

		addToNodesAndTop(state, new GSSNode(state));
	}

	public boolean isAny() {
		if (nodes.size() != 1)
			return false;

		return nodes.iterator().next().isAny();
	}

	public GSSNode move(final Integer from, final Integer to) {
		assert (top.containsKey(from));

		final GSSNode node = top.get(from);
		final GSSNode newNode = top.containsKey(to) ? top.get(to) : node.cloneMoved(to);

		addToNodesAndTop(to, newNode);
		removeNode(node);

		return newNode;
	}

	public void call(final Integer from, final Integer returnState, final Integer startState) {
		assert (top.containsKey(from));

		final GSSNode returnNode = move(from, returnState);
		final GSSNode newStackTop = top.containsKey(startState) ? top.get(startState) : new GSSNode(startState);

		returnNode.calls.add(newStackTop);
		newStackTop.callers.add(returnNode);

		top.remove(returnState);
		addToNodesAndTop(startState, newStackTop);
	}

	public void returnFromCall(final Integer state) {
		assert (top.containsKey(state));

		final GSSNode stackTop = top.get(state);
		for (GSSNode caller : stackTop.callers)
			caller.calls.remove(stackTop);

		for (GSSNode caller : stackTop.callers)
			top.put(caller.state, caller);

		top.remove(state);
		nodes.remove(stackTop);
	}

	private void addToNodesAndTop(final Integer state, final GSSNode node) {
		nodes.add(node);
		top.put(state, node);
	}

	private void removeNode(final GSSNode node) {
		assert (top.containsValue(node));
		node.disconnect();
		top.remove(node.state);
	}

	public String toGraphviz() {
		final StringBuilder result = new StringBuilder();

		result.append("digraph graph_structured_stack {\n");
		result.append("  rankdir=S;\n");
		result.append("  size=\"8,5\"\n");
		result.append("  node [shape = square];\n");

		for (final GSSNode t : top.values())
			result.append("  S" + t.state + "\n");

		result.append(";\n");

		result.append("  node [shape = circle];\n");
		for (final GSSNode node : nodes)
			for (GSSNode call : node.calls)
				result.append("  S" + node.state + " -> S" + call.state + ";\n");

		result.append("}\n");

		return result.toString();
	}

	@Override
	public String toString() {
		final StringBuilder result = new StringBuilder();

		for (final GSSNode topNode : this.top.values()) {
			result.append(topNode.toString());
			result.append("\r\n");
		}

		return result.toString();
	}
}
