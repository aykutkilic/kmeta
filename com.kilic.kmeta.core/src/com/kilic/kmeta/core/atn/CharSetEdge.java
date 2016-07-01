package com.kilic.kmeta.core.atn;

import com.kilic.kmeta.core.util.CharSet;

public class CharSetEdge extends ATNEdgeBase {
	CharSet charSet;

	CharSetEdge(CharSet charSet) {
		this.charSet = charSet;
	}
}
