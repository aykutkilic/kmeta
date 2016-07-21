package com.kilic.kmeta.core.alls.atn;

import com.kilic.kmeta.core.alls.dfa.DFA;
import com.kilic.kmeta.core.alls.stream.IStream;

public class ATNTokenEdge extends ATNEdgeBase {
	DFA tokenDFA;

	ATNTokenEdge(ATNState from, ATNState to, DFA dfa) {
		this.tokenDFA = dfa;
		connect(from, to);
	}

	public DFA getTokenDFA() {
		return tokenDFA;
	}

	@Override
	public String match(IStream input) {
		return tokenDFA.lookAhead(input);
	}

	@Override
	public String getLabel() {
		return tokenDFA.getLabel();
	}
}
