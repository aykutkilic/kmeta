package com.kilic.kmeta.core.syntax;

import com.kilic.kmeta.core.atn.ATN;
import com.kilic.kmeta.core.atn.ATNState;
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

		nfa.createMatcherTransition(preSepState, secondEState, new StringMatcher(separator));

		nfa.createEpsilonTransition(sourceState, targetState);
		nfa.createEpsilonTransition(preSepState, targetState);
		nfa.createEpsilonTransition(preTargetState, preSepState);
		nfa.createEpsilonTransition(preTargetState, targetState);

		return targetState;
	}

	@Override
	public ATNState appendToATN(ATN atn, ATNState sourceState, ATNState targetState) {
		ATNState preSepState = atn.createState();
		ATNState secondEState = atn.createState();
		ATNState preTargetState = atn.createState();

		if (targetState == null)
			targetState = atn.createState();

		expr.appendToATN(atn, sourceState, preSepState);
		expr.appendToATN(atn, secondEState, preTargetState);

		atn.createStringEdge(preSepState, secondEState, separator);

		atn.createEpsilonEdge(sourceState, targetState);
		atn.createEpsilonEdge(preSepState, targetState);
		atn.createEpsilonEdge(preTargetState, preSepState);
		atn.createEpsilonEdge(preTargetState, targetState);

		return targetState;
	}
}
