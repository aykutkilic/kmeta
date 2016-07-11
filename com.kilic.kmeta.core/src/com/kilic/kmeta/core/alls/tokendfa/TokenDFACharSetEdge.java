package com.kilic.kmeta.core.alls.tokendfa;

import com.kilic.kmeta.core.alls.tn.CharSetEdgeBase;
import com.kilic.kmeta.core.util.CharSet;

public class TokenDFACharSetEdge extends CharSetEdgeBase<TokenDFAState> implements ITokenDFAEdge {
	protected TokenDFACharSetEdge(TokenDFAState from, TokenDFAState to, CharSet charSet) {
		super(from, to, charSet);
	}
}
