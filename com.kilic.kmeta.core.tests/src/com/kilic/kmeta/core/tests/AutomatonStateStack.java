package com.kilic.kmeta.core.tests;

import java.util.Stack;

import com.kilic.kmeta.core.dfa.DFAState;

public class AutomatonStateStack {
	Stack<DFAState> stack;

	public AutomatonStateStack(DFAState... states) {
		stack = new Stack<>();
		for (DFAState state : states) {
			stack.push(state);
		}
	}

	public Stack<DFAState> getStack() {
		return stack;
	}
}
