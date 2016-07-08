package com.kilic.kmeta.core.dfa;

import com.kilic.kmeta.core.stream.IStream;

public class DFAStringEdge<SK> extends DFAEdgeBase<SK> {
	String string;
	
	DFAStringEdge(String string) {
		this.string = string;
	}
	
	@Override
	public boolean move(IStream input) {
		return false;
	}

	@Override
	public String getLabel() {
		return string;
	}

}
