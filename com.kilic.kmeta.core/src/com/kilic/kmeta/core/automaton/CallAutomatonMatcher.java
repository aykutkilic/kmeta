package com.kilic.kmeta.core.automaton;

import com.kilic.kmeta.core.stream.IStream;

public class CallAutomatonMatcher implements IMatcher {
	Automaton automaton;

	public CallAutomatonMatcher(Automaton automaton) {
		this.automaton = automaton;
	}

	public Automaton getAutomaton() {
		return automaton;
	}

	@Override
	public boolean match(IStream stream) {
		return false;
	}

}
