package com.kilic.kmeta.core.syntax;

import com.kilic.kmeta.core.dfa.Automaton;
import com.kilic.kmeta.core.dfa.AutomatonState;

public class ParenExpr implements ISyntaxExpr {
	ISyntaxExpr expr;

	public ISyntaxExpr getExpr() {
		return expr;
	}

	public void setExpr(ISyntaxExpr expr) {
		this.expr = expr;
	}

	@Override
	public AutomatonState appendToDFA(Automaton dfa, AutomatonState sourceState, AutomatonState targetState) {
		if (targetState == null)
			targetState = dfa.createState();

		expr.appendToDFA(dfa, sourceState, targetState);

		return targetState;
	}
}
