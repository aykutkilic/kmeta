package com.kilic.kmeta.core.atn;

import com.kilic.kmeta.core.stream.IStream;

public class ATNMutatorEdge extends ATNEdgeBase {

	@Override
	public String getLabel() {
		return "mut";
	}

	@Override
	public boolean move(IStream input) {
		return false;
	}
}
