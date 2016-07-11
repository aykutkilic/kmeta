package com.kilic.kmeta.core.alls.atn;

import com.kilic.kmeta.core.alls.tn.StringEdgeBase;

public class ATNStringEdge extends StringEdgeBase<ATNState> implements IATNEdge {
	public ATNStringEdge(ATNState from, ATNState to, String string) {
		super(from, to, string);
	}
}
