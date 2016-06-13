package com.kilic.kmeta.core.automaton;

import java.util.Stack;

public class AutomatonRunState implements Cloneable {
	Automaton automaton;
	Stack<AutomatonState> callStack;

	public AutomatonRunState(Automaton automaton) {
		this.automaton = automaton;
		callStack.push(automaton.startState);
	}
	
	private AutomatonRunState() {}
	
	public Automaton getAutomaton() {
		return automaton;
	}
	
	void call(CallAutomatonTransition transition) {
		assert(transition.getFromState() == callStack.peek());
		callStack.pop();
		callStack.push(transition.getToState());
		callStack.push(transition.getAutomaton().getStartState());
	}

	void returnFromCall() {
		callStack.pop();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Object clone() throws CloneNotSupportedException {
		AutomatonRunState clone = new AutomatonRunState();
		
		clone.callStack = (Stack<AutomatonState>) callStack.clone();
		
		return clone;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof AutomatonRunState) {
			return callStack.equals(((AutomatonRunState) other).callStack);
		}

		return false;
	}
}
