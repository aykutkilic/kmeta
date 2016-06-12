package com.kilic.kmeta.core.automaton;

import java.util.Stack;

public abstract class AutomatonRunnerBase implements IAutomatonRunner {
	Automaton automaton;
	Stack<AutomatonState> callStack = new Stack<>();

	public AutomatonRunnerBase(Automaton automaton) {
		this.automaton = automaton;
	}

	public void start() {
		assert (callStack.isEmpty());
		callStack.push(automaton.getStartState());
	}

	public AutomatonState getCurrentState() {
		if (callStack.isEmpty())
			return null;

		return callStack.peek();
	}

	public Stack<AutomatonState> getCallStack() {
		return callStack;
	}
}
