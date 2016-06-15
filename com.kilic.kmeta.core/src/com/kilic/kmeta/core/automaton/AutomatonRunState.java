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
		if (topElem instanceof AutomatonState)
			return topElem.isFinalState();

		return false;
	}

	@Override
	public int hashCode() {
		int hashCode = 1;
		for( AutomatonState s : callStack)
			hashCode = hashCode*31 + s.hashCode();
		
		return hashCode;
	}

	@Override
	public boolean equals(Object other) {
		if( this == other ) return true;
		if (!(other instanceof AutomatonRunState))
			return false;
		
		return callStack.equals(((AutomatonRunState) other).callStack);
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();

		result.append("<");
		for (AutomatonState stackElement : callStack)
			result.append(stackElement.toString() + " ");
		result.append(">");

		return result.toString();
	}
}
