package com.kilic.kmeta.core.tokendfa;

import com.kilic.kmeta.core.tn.StateBase;

public class TokenDFAState extends StateBase<Integer> {
	protected TokenDFAState(TokenDFA container, Integer stateKey) {
		super(container, stateKey);
	}
}
