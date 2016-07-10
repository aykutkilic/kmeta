package com.kilic.kmeta.core.alls.tokendfa;

import com.kilic.kmeta.core.alls.tn.IntegerKeyedState;

public class TokenDFAState extends IntegerKeyedState<ITokenDFAEdge, TokenDFAState> {
	protected TokenDFAState(TokenDFA container) {
		super(container);
	}
}
