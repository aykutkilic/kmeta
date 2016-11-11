package com.kilic.kmeta.core.alls.analysis;

import java.util.HashSet;
import java.util.Set;

public class GSSNode {
	final Integer state;
	Set<GSSNode> callers, calls;

	GSSNode(final Integer state) {
		this.state = state;
		callers = new HashSet<>();
		calls = new HashSet<>();
	}

	public boolean isAny() {
		return state == GSS.ANY_STACK;
	}

	GSSNode cloneMoved(final Integer toState) {
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

	@Override
	public int hashCode() {
		return state;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof GSSNode))
			return false;
		return this.state.equals(((GSSNode) other).state);
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();

		result.append("( " + this.state + " ");

		for (GSSNode caller : this.callers)
			result.append(" <" + caller.state);

		for (GSSNode call : this.calls)
			result.append(" >" + call.state);

		result.append(" )");

		return result.toString();
	}
}
