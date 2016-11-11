package com.kilic.kmeta.core.syntax;

import com.kilic.kmeta.core.alls.atn.ATN;
import com.kilic.kmeta.core.alls.atn.ATNState;

public class ATNCallExpr implements ISyntaxExpr {
	private final ATN calledATN;

	public ATNCallExpr(final ATN atn) {
		this.calledATN = atn;
	}

	@Override
	public ATNState appendToATN(final ATN atn, final ATNState sourceState, ATNState targetState) {
		if (targetState == null)
			targetState = atn.createState();

		atn.createCallEdge(sourceState, targetState, this.calledATN);

		return targetState;
	}
}
