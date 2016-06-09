package com.kilic.kmeta.core.syntax;

import com.kilic.kmeta.core.dfa.DFA;
import com.kilic.kmeta.core.dfa.DFAState;
import com.kilic.kmeta.core.dfa.StringMatcher;

public class SeparatorExpr implements ISyntaxExpr {
	String separator;
	ISyntaxExpr expr;

	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}

	public ISyntaxExpr getExpr() {
		return expr;
	}

	public void setExpr(ISyntaxExpr expr) {
		this.expr = expr;
	}

	@Override
	public DFAState appendToDFA(DFA dfa, DFAState sourceState, DFAState targetState) {
		DFAState preSepState = dfa.createState();
		DFAState secondEState = dfa.createState();
		if (targetState == null)
			targetState = dfa.createState();

		expr.appendToDFA(dfa, sourceState, preSepState);
		expr.appendToDFA(dfa, secondEState, targetState);

		dfa.createTransition(preSepState, secondEState, new StringMatcher(separator));

		dfa.createTransition(sourceState, targetState, null);
		dfa.createTransition(preSepState, targetState, null);
		dfa.createTransition(targetState, preSepState, null);

		return targetState;
	}
}
