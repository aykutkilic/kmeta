package com.kilic.kmeta.core.automaton;

import java.util.Arrays;

public class AutomatonRunState {
	AutomatonState[] callStack;

	public AutomatonRunState(Automaton automaton) {
		callStack = new AutomatonState[1];
		callStack[0] = automaton.startState;
	}

	private AutomatonRunState() {
	}

	AutomatonRunState call(CallAutomatonTransition transition) {
		AutomatonRunState result = new AutomatonRunState();
		Automaton automaton = transition.getAutomaton();

		result.callStack = Arrays.copyOf(callStack, callStack.length + 1);
		result.callStack[result.callStack.length - 1] = automaton.startState;

		return result;
	}

	AutomatonRunState returnFromCall() {
		AutomatonRunState result = new AutomatonRunState();

		result.callStack = Arrays.copyOf(callStack, callStack.length - 1);
		AutomatonState lastState = result.callStack[result.callStack.length - 1];

		return result;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof AutomatonRunState) {
			return callStack.equals(((AutomatonRunState) other).callStack);
		}

		return false;
	}
}
