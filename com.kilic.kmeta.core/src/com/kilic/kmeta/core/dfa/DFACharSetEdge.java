package com.kilic.kmeta.core.dfa;

import com.kilic.kmeta.core.stream.IStream;
import com.kilic.kmeta.core.util.CharSet;

public class DFACharSetEdge<SK> extends DFAEdgeBase<SK> {
	CharSet charSet;
	
	DFACharSetEdge(CharSet charSet) {
		this.charSet = charSet;
	}
	
	@Override
	public boolean move(IStream input) {
		return false;
	}

	@Override
	public String getLabel() {
		return charSet.toString();
	}
}
