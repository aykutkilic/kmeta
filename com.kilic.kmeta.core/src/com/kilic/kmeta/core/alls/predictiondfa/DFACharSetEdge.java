package com.kilic.kmeta.core.alls.predictiondfa;

import com.kilic.kmeta.core.alls.stream.IStream;
import com.kilic.kmeta.core.util.CharSet;

public class DFACharSetEdge<K> extends DFAEdgeBase<K> {
	CharSet charSet;
	
	DFACharSetEdge(CharSet charSet) {
		this.charSet = charSet;
	}

	@Override
	public String getLabel() {
		return charSet.toString();
	}
}
