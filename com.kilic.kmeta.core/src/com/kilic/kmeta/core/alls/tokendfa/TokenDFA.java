package com.kilic.kmeta.core.alls.tokendfa;

import com.kilic.kmeta.core.alls.automaton.AutomatonBase;
import com.kilic.kmeta.core.alls.stream.IStream;
import com.kilic.kmeta.core.util.CharSet;

public class TokenDFA extends AutomatonBase<Integer, ITokenDFAEdge, TokenDFAState> {
	public TokenDFAState createState() {
		return new TokenDFAState(this);
	}
	
	public void createCharSetEdge(TokenDFAState from, TokenDFAState to, CharSet charSet) {
		connectEdge(from, to, new TokenDFACharSetEdge(charSet));
	}
	
	public boolean matches(IStream stream) {
		return false;
	}
}
