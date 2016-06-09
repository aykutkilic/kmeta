package com.kilic.kmeta.core.syntax;

import com.kilic.kmeta.core.dfa.DFA;
import com.kilic.kmeta.core.dfa.DFAState;

public class ParenExpr implements ISyntaxExpr {
	ISyntaxExpr expr;

	public ISyntaxExpr getExpr() {
		return expr;
	}

	public void setExpr(ISyntaxExpr expr) {
		this.expr = expr;
	}

	@Override
	public DFAState appendToDFA(DFA dfa, DFAState sourceState, DFAState targetState) {
		if (targetState == null)
			targetState = dfa.createState();

		expr.appendToDFA(dfa, sourceState, targetState);

		return targetState;
	}
}
