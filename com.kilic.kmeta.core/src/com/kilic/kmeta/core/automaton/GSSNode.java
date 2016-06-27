package com.kilic.kmeta.core.automaton;

import java.util.HashSet;
import java.util.Set;

class GSSNode {
	Integer state;
	Set<GSSNode> callers, calls;

	GSSNode(Integer state) {
		this.state = state;
		callers = new HashSet<>();
		calls = new HashSet<>();
	}

	GSSNode cloneMoved(Integer toState) {
		GSSNode result = new GSSNode(toState);
		result.callers = new HashSet<>(callers);
		result.calls = new HashSet<>(calls);

		for (GSSNode caller : result.callers)
			caller.calls.add(result);

		for (GSSNode call : result.calls)
			call.callers.add(result);

		return result;
	}

	void disconnect() {
		for (GSSNode caller : callers)
			caller.calls.remove(this);

		for (GSSNode call : calls)
			call.callers.remove(this);
	}

	Integer getState() {
		return state;
	}
}
