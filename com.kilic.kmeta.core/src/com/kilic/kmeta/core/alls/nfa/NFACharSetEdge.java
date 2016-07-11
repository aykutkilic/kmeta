package com.kilic.kmeta.core.alls.nfa;

import com.kilic.kmeta.core.alls.tn.CharSetEdgeBase;
import com.kilic.kmeta.core.util.CharSet;

public class NFACharSetEdge extends CharSetEdgeBase<NFAState> implements INFAEdge {
	protected NFACharSetEdge(NFAState from, NFAState to, CharSet charSet) {
		super(from, to, charSet);
	}

}
