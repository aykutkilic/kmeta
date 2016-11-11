package com.kilic.kmeta.core.syntax;

import com.kilic.kmeta.core.alls.atn.ATN;
import com.kilic.kmeta.core.alls.atn.ATNState;

public class ParenExpr implements ISyntaxExpr {
	private ISyntaxExpr expr;

	public ISyntaxExpr getExpr() {
		return expr;
	}

	public void setExpr(final ISyntaxExpr expr) {
		this.expr = expr;
	}

	@Override
	public ATNState appendToATN(final ATN atn, final ATNState sourceState, ATNState targetState) {
		if (targetState == null)
			targetState = atn.createState();

		expr.appendToATN(atn, sourceState, targetState);

		return targetState;
	}
}
