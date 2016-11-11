package com.kilic.kmeta.core.alls.atn;

import com.kilic.kmeta.core.alls.stream.IStream;
import com.kilic.kmeta.core.alls.tn.EpsilonEdgeBase;

public class ATNEpsilonEdge extends EpsilonEdgeBase<ATNState> implements IATNEdge {
	protected ATNEpsilonEdge(final ATNState from, final ATNState to) {
		super(from, to);
	}

	@Override
	public String match(final IStream input) {
		if (input.hasEnded() && this.getTo().isFinalState())
			return "";

		return super.match(input);
	}
}
