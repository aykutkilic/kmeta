package com.kilic.kmeta.core.dfa;

import com.kilic.kmeta.core.stream.IStream;
import com.kilic.kmeta.core.util.CharSet;

public class DFACharSetEdge extends DFAEdgeBase {
	CharSet charSet;
	
	DFACharSetEdge(CharSet charSet) {
		this.charSet = charSet;
	}
	
	@Override
	public boolean move(IStream input) {
		return false;
	}
}
