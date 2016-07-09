package com.kilic.kmeta.core.alls.atn;

import com.kilic.kmeta.core.alls.stream.IStream;
import com.kilic.kmeta.core.alls.tokendfa.TokenDFA;

public class ATNTokenEdge extends ATNEdgeBase {
	TokenDFA dfa;
	
	ATNTokenEdge(TokenDFA dfa) {
		this.dfa = dfa;
	}
	
	TokenDFA getDFA() {
		return dfa;
	}
	
	@Override
	public boolean moves(IStream input) {
		return dfa.matches(input);
	}

	@Override
	public String getLabel() {
		return dfa.getLabel();
	}

}
