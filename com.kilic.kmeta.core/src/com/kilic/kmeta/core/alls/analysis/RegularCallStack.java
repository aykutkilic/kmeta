package com.kilic.kmeta.core.alls.analysis;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

import com.kilic.kmeta.core.alls.atn.ATNState;

public class RegularCallStack {
	public static final int ANY_STACK = -1;

	private Stack<Integer> callStack;
	private Map<Integer, ATNState> states;

	public static RegularCallStack newAnyStack() {
		final RegularCallStack result = new RegularCallStack();
		result.pushAny();
		return result;
	}

	public RegularCallStack() {
		callStack = new Stack<>();
		states = new HashMap<>();
	}

	public RegularCallStack(final RegularCallStack other) {
		this.callStack = new Stack<>();
		this.callStack.addAll(other.callStack);
		this.states = new HashMap<>(other.states);
	}

	public void pushAny() {
		callStack.push(ANY_STACK);
	}

	public boolean isAny() {
		return callStack.peek() == ANY_STACK;
	}

	public boolean isEmpty() {
		return callStack.isEmpty();
	}

	public void push(ATNState state) {
		callStack.push(state.getKey());
		states.put(state.getKey(), state);
	}

	public ATNState pop() {
		int stateIndex = callStack.pop();

		final ATNState result = states.get(stateIndex);
		// states.remove(stateIndex);

		return result;
	}

	public ATNState peek() {
		if (callStack.isEmpty())
			return null;
		return states.get(callStack.peek());
	}

	@Override
	public int hashCode() {
		return callStack.hashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;

		if (!(other instanceof RegularCallStack))
			return false;

		final RegularCallStack o = (RegularCallStack) other;

		return callStack.equals(o.callStack);
	}

	@Override
	public String toString() {
		final StringBuilder result = new StringBuilder();

		result.append("<");
		final Iterator<Integer> i = callStack.iterator();
		while (i.hasNext()) {
			Integer index = i.next();
			result.append(index != ANY_STACK ? index : "#");
			if (i.hasNext())
				result.append(",");
		}
		result.append(">");

		return result.toString();
	}
}
