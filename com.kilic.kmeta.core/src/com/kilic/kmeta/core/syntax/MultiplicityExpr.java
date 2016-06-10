package com.kilic.kmeta.core.syntax;

import com.kilic.kmeta.core.dfa.Automaton;
import com.kilic.kmeta.core.dfa.AutomatonState;
import com.kilic.kmeta.core.meta.Multiplicity;

public class MultiplicityExpr implements ISyntaxExpr {
	ISyntaxExpr expr;
	Multiplicity multiplicity;

	public MultiplicityExpr(Multiplicity multiplicity, ISyntaxExpr expr) {
		this.multiplicity = multiplicity;
		this.expr = expr;
	}

	public ISyntaxExpr getExpr() {
		return expr;
	}

	public void setExpr(ISyntaxExpr expr) {
		this.expr = expr;
	}

	public Multiplicity getMultiplicity() {
		return multiplicity;
	}

	public void setMultiplicity(Multiplicity mult) {
		this.multiplicity = mult;
	}

	@Override
	public AutomatonState appendToNFA(Automaton nfa, AutomatonState sourceState, AutomatonState targetState) {
		if (targetState == null)
			targetState = nfa.createState();

		AutomatonState exprStartState = sourceState;
		AutomatonState exprEndState = targetState;

		expr.appendToNFA(nfa, exprStartState, exprEndState);

		switch (multiplicity) {
		case ONE:
			break;

		case OPTIONAL:
			nfa.createTransition(exprStartState, exprEndState, null);
			break;

		case ANY:
			nfa.createTransition(exprStartState, exprEndState, null);
			nfa.createTransition(exprEndState, exprStartState, null);
			break;

		case ONEORMORE:
			nfa.createTransition(exprEndState, exprStartState, null);
			break;
		}

		return targetState;
	}
}
