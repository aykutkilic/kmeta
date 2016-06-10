package com.kilic.kmeta.core.syntax;

import com.kilic.kmeta.core.dfa.Automaton;
import com.kilic.kmeta.core.dfa.AutomatonState;
import com.kilic.kmeta.core.dfa.StringMatcher;

public class SeparatorExpr implements ISyntaxExpr {
	String separator;
	ISyntaxExpr expr;

	public SeparatorExpr(ISyntaxExpr expr, String separator) {
		this.expr = expr;
		this.separator = separator;
	}

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
	public AutomatonState appendToNFA(Automaton nfa, AutomatonState sourceState, AutomatonState targetState) {
		AutomatonState preSepState = nfa.createState();
		AutomatonState secondEState = nfa.createState();
		AutomatonState preTargetState = nfa.createState();

		if (targetState == null)
			targetState = nfa.createState();

		expr.appendToNFA(nfa, sourceState, preSepState);
		expr.appendToNFA(nfa, secondEState, preTargetState);

		nfa.createTransition(preSepState, secondEState, new StringMatcher(separator));

		nfa.createTransition(sourceState, targetState, null);
		nfa.createTransition(preSepState, targetState, null);
		nfa.createTransition(preTargetState, preSepState, null);
		nfa.createTransition(preTargetState, targetState, null);

		return targetState;
	}
}
