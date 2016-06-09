package com.kilic.kmeta.core.dfa;

import com.kilic.kmeta.core.stream.IStream;

public class SubDFACallMatcher implements IMatcher {
	DFA subDFA;

	public SubDFACallMatcher(DFA subDFA) {
		this.subDFA = subDFA;
	}

	@Override
	public boolean match(IStream stream) {
		return subDFA.match(stream);
	}
}
