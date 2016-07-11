package com.kilic.kmeta.core.alls.tokendfa;

import com.kilic.kmeta.core.alls.automaton.AutomatonBase;
import com.kilic.kmeta.core.alls.stream.IStream;
import com.kilic.kmeta.core.util.CharSet;

public class TokenDFA extends AutomatonBase<Integer, ITokenDFAEdge, TokenDFAState> {
	public TokenDFAState createState() {
		TokenDFAState newState = new TokenDFAState(this);
		states.put(newState.getKey(), newState);
		return newState;
	}
	
	public TokenDFACharSetEdge createCharSetEdge(TokenDFAState from, TokenDFAState to, CharSet charSet) {
		return new TokenDFACharSetEdge(from, to, charSet);
	}
	
	public boolean matches(IStream stream) {
		return false;
	}
}
