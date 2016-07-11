package com.kilic.kmeta.core.alls.atn;

import com.kilic.kmeta.core.alls.stream.IStream;

public class ATNCallEdge extends ATNEdgeBase {
	ATN atn;

	ATNCallEdge(ATNState from, ATNState to, ATN atn) {
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
	public boolean moves(IStream input) {
		return false;
	}
}
