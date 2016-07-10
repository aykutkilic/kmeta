package com.kilic.kmeta.core.alls.atn;

import com.kilic.kmeta.core.alls.tn.CharSetEdgeBase;
import com.kilic.kmeta.core.util.CharSet;

public class ATNCharSetEdge extends CharSetEdgeBase<ATNState> {
	ATNCharSetEdge(CharSet charSet) {
		super(charSet);
	}
}
