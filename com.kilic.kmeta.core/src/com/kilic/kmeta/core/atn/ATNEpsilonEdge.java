package com.kilic.kmeta.core.atn;

import com.kilic.kmeta.core.stream.IStream;

public class ATNEpsilonEdge extends ATNEdgeBase {

	@Override
	public String getLabel() {
		return "epsilon";
	}

	@Override
	public boolean move(IStream input) {
		return false;
	}
}
