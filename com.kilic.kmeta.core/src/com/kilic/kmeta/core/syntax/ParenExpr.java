package com.kilic.kmeta.core.syntax;

import com.kilic.kmeta.core.atn.ATN;
import com.kilic.kmeta.core.atn.IATNState;

public class ParenExpr implements ISyntaxExpr {
	ISyntaxExpr expr;

	public ISyntaxExpr getExpr() {
		return expr;
	}

	public void setExpr(ISyntaxExpr expr) {
		this.expr = expr;
	}

	@Override
	public IATNState appendToATN(ATN atn, IATNState sourceState, IATNState targetState) {
		if (targetState == null)
			targetState = atn.createRegularState();

		expr.appendToATN(atn, sourceState, targetState);

		return targetState;
	}
}
