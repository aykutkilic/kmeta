package com.kilic.kmeta.core.automaton;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GSS {
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

	public GSSNode init(Integer state) {
		if (top.containsKey(state))
			return top.get(state);

		GSSNode newNode = new GSSNode(state);

		nodes.add(newNode);
		top.put(state, newNode);

		return newNode;
	}

	public GSSNode move(Integer from, Integer to) {
		assert (top.containsKey(from));

		GSSNode node = top.get(from);
		GSSNode newNode = node.cloneMoved(to);
		removeNode(node);

		return newNode;
	}

	void removeNode(GSSNode node) {
		assert (top.containsValue(node));
		node.disconnect();
		top.remove(node);
	}

	public void call(Integer from, Integer returnState, Integer startState) {
		assert (top.containsKey(from));

		GSSNode returnNode = top.get(from).cloneMoved(returnState);
		GSSNode newStackTop = top.containsKey(startState) ? top.get(startState) : new GSSNode(startState);

		returnNode.calls.add(newStackTop);
		newStackTop.callers.add(returnNode);
	}

	public void returnFromCall(Integer state) {
		assert (top.containsKey(state));

		GSSNode stackTop = top.get(state);
		for (GSSNode caller : stackTop.callers)
			caller.calls.remove(stackTop);

		top.remove(stackTop);
	}
}
