package com.kilic.kmeta.core.dfa;

import com.kilic.kmeta.core.stream.IStream;

public class DFAStringEdge extends DFAEdgeBase {
	String string;
	
	DFAStringEdge(String string) {
		this.string = string;
	}
	
	@Override
	public boolean move(IStream input) {
		return false;
	}

}
