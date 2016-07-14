package com.kilic.kmeta.core.alls.dfa;

import com.kilic.kmeta.core.alls.tn.CharSetEdgeBase;
import com.kilic.kmeta.core.util.CharSet;

public class DFACharSetEdge extends CharSetEdgeBase<DFAState> implements IDFAEdge {
	protected DFACharSetEdge(DFAState from, DFAState to, CharSet charSet) {
		super(from, to, charSet);
	}
}
