package com.kilic.kmeta.core.alls.atn;

import com.kilic.kmeta.core.alls.stream.IStream;

public class ATNCallEdge extends ATNEdgeBase {
	private final ATN atn;

	ATNCallEdge(final ATNState from, final ATNState to, final ATN atn) {
		this.atn = atn;
		atn.addCaller(this);
		connect(from, to);
	}

	public ATN getATN() {
		return atn;
	}

	@Override
	public String getLabel() {
		if (atn.getLabel() != null)
			return "[" + atn.getLabel() + "]";

		return "[??]";
	}

	@Override
	public String match(final IStream input) {
		return null;
	}
}
