package com.kilic.kmeta.core.atn;

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
}
