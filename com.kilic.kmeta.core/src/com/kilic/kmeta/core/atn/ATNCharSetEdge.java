package com.kilic.kmeta.core.atn;

import com.kilic.kmeta.core.stream.IStream;
import com.kilic.kmeta.core.util.CharSet;

public class ATNCharSetEdge extends ATNEdgeBase {
	CharSet charSet;

	ATNCharSetEdge(CharSet charSet) {
		this.charSet = charSet;
	}

	@Override
	public String getLabel() {
		return charSet.toString();
	}

	@Override
	public boolean moves(IStream input) {
		char c = input.lookAheadChar(0);
		return charSet.containsSingleton(c);
	}
}
