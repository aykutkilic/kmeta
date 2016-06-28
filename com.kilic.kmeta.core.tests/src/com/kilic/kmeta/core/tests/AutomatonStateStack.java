package com.kilic.kmeta.core.tests;

import java.util.Stack;

import com.kilic.kmeta.core.dfa.AutomatonState;

public class AutomatonStateStack {
	Stack<AutomatonState> stack;

	public AutomatonStateStack(AutomatonState... states) {
		stack = new Stack<>();
		for (AutomatonState state : states) {
			stack.push(state);
		}
	}

	public Stack<AutomatonState> getStack() {
		return stack;
	}
}
