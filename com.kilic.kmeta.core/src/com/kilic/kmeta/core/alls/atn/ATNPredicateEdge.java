package com.kilic.kmeta.core.alls.atn;

import com.kilic.kmeta.core.alls.stream.IStream;

/**
 * 	Used for semantic and syntactic predicates.
 */
public class ATNPredicateEdge extends ATNEdgeBase {
	ATNPredicateEdge(final ATNState from, final ATNState to) {
		connect(from, to);
	}

	@Override
	public String getLabel() {
		return "pred";
	}

	@Override
	public String match(final IStream input) {
		return null;
	}
}
