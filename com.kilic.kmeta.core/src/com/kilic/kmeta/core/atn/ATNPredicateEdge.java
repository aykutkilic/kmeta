package com.kilic.kmeta.core.atn;

import com.kilic.kmeta.core.stream.IStream;

/**
 * 	Used for semantic and syntactic predicates.
 */
public class ATNPredicateEdge extends ATNEdgeBase {
	@Override
	public String getLabel() {
		return "pred";
	}

	@Override
	public boolean moves(IStream input) {
		return false;
	}
}
