package com.kilic.kmeta.core.alls.atn;

import com.kilic.kmeta.core.alls.stream.IStream;

public class ATNMutatorEdge extends ATNEdgeBase {

	@Override
	public String getLabel() {
		return "mut";
	}

	@Override
	public boolean moves(IStream input) {
		return false;
	}
}