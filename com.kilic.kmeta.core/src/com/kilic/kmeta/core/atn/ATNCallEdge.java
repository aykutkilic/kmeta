package com.kilic.kmeta.core.atn;

public class ATNCallEdge extends ATNEdgeBase {
	ATN atn;

	ATNCallEdge(ATN atn) {
		this.atn = atn;
	}
}