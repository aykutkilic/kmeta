package com.kilic.kmeta.core.alls.tokendfa;

import com.kilic.kmeta.core.alls.tn.StateBase;

public class TokenDFAState extends StateBase<Integer, TokenDFAEdge, TokenDFAState> {
	protected TokenDFAState(TokenDFA container, Integer stateKey) {
		super(container, stateKey);
	}
}
