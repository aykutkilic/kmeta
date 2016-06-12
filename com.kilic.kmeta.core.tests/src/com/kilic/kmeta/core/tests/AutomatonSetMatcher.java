package com.kilic.kmeta.core.tests;

import java.util.HashSet;
import java.util.Set;

import com.kilic.kmeta.core.automaton.Automaton;
import com.kilic.kmeta.core.automaton.IMatcher;
import com.kilic.kmeta.core.stream.IStream;

// for testing purposes.
public class AutomatonSetMatcher implements IMatcher {
	Set<Automaton> automatonSet = new HashSet<>();

	void addAutomaton(Automaton a) {
		automatonSet.add(a);
	}

	@Override
	public boolean match(IStream stream) {
		return false;
	}
}
