package com.kilic.kmeta.core.alls.atn;

import com.kilic.kmeta.core.alls.stream.IStream;
import com.kilic.kmeta.core.alls.tokendfa.TokenDFA;

public class ATNTokenEdge extends ATNEdgeBase {
	TokenDFA dfa;

	ATNTokenEdge(ATNState from, ATNState to, TokenDFA dfa) {
		this.dfa = dfa;
		connect(from, to);
	}

	TokenDFA getDFA() {
		return dfa;
	}

	@Override
	public boolean moves(IStream input) {
		return dfa.lookAhead(input) != null;
	}

	@Override
	public String getLabel() {
		return dfa.getLabel();
	}
}
