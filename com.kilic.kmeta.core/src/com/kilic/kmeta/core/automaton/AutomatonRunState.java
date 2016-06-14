package com.kilic.kmeta.core.automaton;

import java.util.Stack;

public class AutomatonRunState {
	Automaton automaton;
	Stack<AutomatonState> callStack;

	public AutomatonRunState(Automaton automaton) {
		this.automaton = automaton;
		callStack = new Stack<>();
		callStack.push(automaton.startState);
	}

	@SuppressWarnings("unchecked")
	public AutomatonRunState(AutomatonRunState other) {
		callStack = (Stack<AutomatonState>) other.callStack.clone();
	}

	public Automaton getAutomaton() {
		return automaton;
	}

	public Stack<AutomatonState> getCallStack() {
		return callStack;
	}

	public AutomatonState getCurrentLocalState() {
		return callStack.peek();
	}

	public void setCurrentLocalState(AutomatonState newState) {
		callStack.pop();
		callStack.push(newState);
	}

	public void call(CallAutomatonTransition transition) {
		assert (transition.getFromState() == callStack.peek());
		callStack.pop();
		callStack.push(transition.getToState());
		callStack.push(transition.getAutomaton().getStartState());
	}

	public void returnFromCall() {
		callStack.pop();
	}

	public boolean isFinal() {
		if (callStack.size() > 1)
			return false;
		
		AutomatonState topElem = callStack.peek();
		if(topElem instanceof AutomatonState)
			return ((AutomatonState) topElem).isFinalState();
		
		return false;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof AutomatonRunState) {
			return callStack.equals(((AutomatonRunState) other).callStack);
		}

		return false;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();

		result.append("< ");
		for (AutomatonState stackElement : callStack)
			result.append(stackElement.toString() + " ");
		result.append(">");

		return result.toString();
	}
}
