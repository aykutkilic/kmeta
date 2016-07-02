package com.kilic.kmeta.core.syntax;

import com.kilic.kmeta.core.atn.ATN;
import com.kilic.kmeta.core.atn.ATNState;

public class ParenExpr implements ISyntaxExpr {
	ISyntaxExpr expr;

	public ISyntaxExpr getExpr() {
		return expr;
	}

	public void setExpr(ISyntaxExpr expr) {
		this.expr = expr;
	}

	@Override
	public ATNState appendToATN(ATN atn, ATNState sourceState, ATNState targetState) {
		if (targetState == null)
			targetState = atn.createState();

		expr.appendToATN(atn, sourceState, targetState);

		return targetState;
	}
}
