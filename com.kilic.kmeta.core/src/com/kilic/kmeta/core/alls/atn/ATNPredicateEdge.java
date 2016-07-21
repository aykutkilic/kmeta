package com.kilic.kmeta.core.alls.atn;

import com.kilic.kmeta.core.alls.stream.IStream;

/**
 * 	Used for semantic and syntactic predicates.
 */
public class ATNPredicateEdge extends ATNEdgeBase {
	ATNPredicateEdge(ATNState from, ATNState to) {
		connect(from, to);
	}

	@Override
	public String getLabel() {
		return "pred";
	}

	@Override
	public String match(IStream input) {
		return null;
	}
}
