package com.kilic.kmeta.core.syntax;

import com.kilic.kmeta.core.dfa.Automaton;
import com.kilic.kmeta.core.dfa.AutomatonState;
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
	public AutomatonState appendToDFA(Automaton dfa, AutomatonState sourceState, AutomatonState targetState) {
		AutomatonState preSepState = dfa.createState();
		AutomatonState secondEState = dfa.createState();
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
