package com.kilic.kmeta.core.automaton;

import com.kilic.kmeta.core.stream.IStream;

public class SubDFACallMatcher implements IMatcher {
	Automaton subDFA;

	public SubDFACallMatcher(Automaton subDFA) {
		this.subDFA = subDFA;
	}

	@Override
	public boolean match(IStream stream) {
		return subDFA.match(stream);
	}
}
