package com.kilic.kmeta.core.tests;

import java.util.HashSet;
import java.util.Set;

import com.kilic.kmeta.core.dfa.Automaton;
import com.kilic.kmeta.core.dfa.IMatcher;
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

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("[ ");

		for (Automaton automaton : automatonSet) {
			String label = automaton.getLabel();
			if (label != null)
				result.append(label + " ");
		}

		result.append("]");

		return result.toString();
	}
}
