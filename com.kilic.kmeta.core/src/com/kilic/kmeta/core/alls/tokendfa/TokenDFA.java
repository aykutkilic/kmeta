package com.kilic.kmeta.core.alls.tokendfa;

import com.kilic.kmeta.core.alls.atn.ATN;
import com.kilic.kmeta.core.alls.dfa.DFABase;
import com.kilic.kmeta.core.alls.stream.IStream;
import com.kilic.kmeta.core.util.CharSet;

public class TokenDFA extends DFABase<Integer,TokenDFAEdge,TokenDFAState> {
	public static TokenDFA createFromATN(ATN atn) {
		return null;
	}
	
	public static TokenDFA fromString(String string) {
		TokenDFA result = new TokenDFA();
		return result;
	}
	
	public static TokenDFA fromCharSet(CharSet cs) {
		TokenDFA result = new TokenDFA();
		return result;
	}
	
	public boolean matches(IStream stream) {
		return false;
	}
}
