package com.kilic.kmeta.core.alls.tokendfa;

import com.kilic.kmeta.core.alls.tn.CharSetEdgeBase;
import com.kilic.kmeta.core.util.CharSet;

public class TokenDFACharSetEdge extends CharSetEdgeBase<TokenDFAState> {
	protected TokenDFACharSetEdge(CharSet charSet) {
		super(charSet);
	}
}
