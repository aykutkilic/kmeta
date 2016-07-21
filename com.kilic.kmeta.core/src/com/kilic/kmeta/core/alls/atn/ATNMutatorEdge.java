package com.kilic.kmeta.core.alls.atn;

import com.kilic.kmeta.core.alls.stream.IStream;

public class ATNMutatorEdge extends ATNEdgeBase {
	ATNMutatorEdge(ATNState from, ATNState to) {
		connect(from, to);
	}

	@Override
	public String getLabel() {
		return "mut";
	}

	@Override
	public String match(IStream input) {
		return null;
	}
}
