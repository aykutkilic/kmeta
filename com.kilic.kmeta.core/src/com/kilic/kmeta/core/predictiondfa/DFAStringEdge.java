package com.kilic.kmeta.core.predictiondfa;

import com.kilic.kmeta.core.stream.IStream;

public class DFAStringEdge<SK> extends DFAEdgeBase<SK> {
	String string;
	
	DFAStringEdge(String string) {
		this.string = string;
	}

	@Override
	public String getLabel() {
		return string;
	}

}
