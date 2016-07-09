package com.kilic.kmeta.core.analysis;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

// Graph Structured Stack
// Used for step locked execution of ATNs or DFA.
public class GSS {
	public static final int ANY_STACK = -1;
	
	Map<Integer, GSSNode> top;
	Set<GSSNode> nodes;
	
	public GSS() {
		top = new HashMap<>();
		nodes = new HashSet<>();
	}

	public GSS(GSS other) {
		top = new HashMap<>(other.top);
		nodes = new HashSet<>(other.nodes);
	}
	
	public Collection<GSSNode> getStackTops() {
		return top.values();
	}

	public void merge(Integer state) {
		if(state == ANY_STACK) {
			nodes.clear();
			top.clear();
		}
		
		if (top.containsKey(state))
			return;

		GSSNode newNode = new GSSNode(state);
		addToNodesAndTop(state, newNode);
	}
	
	public boolean isAny() {
		if(nodes.size() != 1) return false;
		return nodes.iterator().next().isAny();
	}

	public GSSNode move(Integer from, Integer to) {
		assert (top.containsKey(from));

		GSSNode node = top.get(from);
		GSSNode newNode = top.containsKey(to) ? top.get(to) : node.cloneMoved(to);

		addToNodesAndTop(to, newNode);
		removeNode(node);

		return newNode;
	}

	public void call(Integer from, Integer returnState, Integer startState) {
		assert (top.containsKey(from));

		GSSNode returnNode = move(from, returnState);
		GSSNode newStackTop = top.containsKey(startState) ? top.get(startState) : new GSSNode(startState);

		returnNode.calls.add(newStackTop);
		newStackTop.callers.add(returnNode);

		top.remove(returnState);
		addToNodesAndTop(startState, newStackTop);
	}

	public void returnFromCall(Integer state) {
		assert (top.containsKey(state));

		GSSNode stackTop = top.get(state);
		for (GSSNode caller : stackTop.callers)
			caller.calls.remove(stackTop);

		for (GSSNode caller : stackTop.callers) {
			top.put(caller.state, caller);
		}

		top.remove(state);
		nodes.remove(stackTop);
	}

	void addToNodesAndTop(Integer state, GSSNode node) {
		nodes.add(node);
		top.put(state, node);
	}

	void removeNode(GSSNode node) {
		assert (top.containsValue(node));
		node.disconnect();
		top.remove(node.state);
	}

	public String toGraphviz() {
		StringBuilder result = new StringBuilder();

		result.append("digraph graph_structured_stack {\n");
		result.append("  rankdir=S;\n");
		result.append("  size=\"8,5\"\n");
		result.append("  node [shape = square];\n");

		for (GSSNode t : top.values()) {
			result.append("  S" + t.state + "\n");
		}

		result.append(";\n");

		result.append("  node [shape = circle];\n");
		for (GSSNode node : nodes) {
			for (GSSNode call : node.calls)
				result.append("  S" + node.state + " -> S" + call.state + ";\n");
		}

		result.append("}\n");

		return result.toString();
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();

		for (GSSNode topNode : this.top.values()) {
			result.append(topNode.toString());
			result.append("\r\n");
		}

		return result.toString();
	}
}
