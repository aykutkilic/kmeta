package com.kilic.kmeta.core.atn;

import com.kilic.kmeta.core.stream.IStream;

public class ATNStringEdge extends ATNEdgeBase {
	String string;

	ATNStringEdge(String string) {
		this.string = string;
	}

	@Override
	public String getLabel() {
		return "'" + string +  "'";
	}

	@Override
	public boolean moves(IStream input) {
		String s = input.lookAheadString(0, string.length());
		return string.equals(s);
	}
}
