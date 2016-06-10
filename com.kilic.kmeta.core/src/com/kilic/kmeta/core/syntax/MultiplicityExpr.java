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
	public AutomatonState appendToDFA(Automaton dfa, AutomatonState sourceState, AutomatonState targetState) {
		if (targetState == null)
			targetState = dfa.createState();

		AutomatonState exprStartState = sourceState;
		AutomatonState exprEndState = targetState;

		// dfa.createTransition(sourceState, exprStartState, null);
		// dfa.createTransition(exprEndState, targetState, null);

		expr.appendToDFA(dfa, exprStartState, exprEndState);

		switch (multiplicity) {
		case ONE:
			break;

		case OPTIONAL:
			dfa.createTransition(exprStartState, exprEndState, null);
			break;

		case ANY:
			dfa.createTransition(exprStartState, exprEndState, null);
			dfa.createTransition(exprEndState, exprStartState, null);
			break;

		case ONEORMORE:
			dfa.createTransition(exprEndState, exprStartState, null);
			break;
		}

		return targetState;
	}
}
