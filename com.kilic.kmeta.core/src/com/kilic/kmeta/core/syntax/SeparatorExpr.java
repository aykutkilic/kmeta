package com.kilic.kmeta.core.syntax;

import com.kilic.kmeta.core.automaton.Automaton;
import com.kilic.kmeta.core.automaton.AutomatonState;
import com.kilic.kmeta.core.automaton.StringMatcher;

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

		nfa.createMatcherTransition(preSepState, secondEState, new StringMatcher(separator));

		nfa.createMatcherTransition(sourceState, targetState, null);
		nfa.createMatcherTransition(preSepState, targetState, null);
		nfa.createMatcherTransition(preTargetState, preSepState, null);
		nfa.createMatcherTransition(preTargetState, targetState, null);

		return targetState;
	}
}
