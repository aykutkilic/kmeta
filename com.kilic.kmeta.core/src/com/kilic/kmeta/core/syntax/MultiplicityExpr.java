package com.kilic.kmeta.core.syntax;

import com.kilic.kmeta.core.atn.ATN;
import com.kilic.kmeta.core.atn.ATNState;
import com.kilic.kmeta.core.dfa.DFA;
import com.kilic.kmeta.core.dfa.DFAState;
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
	public ATNState appendToATN(ATN atn, ATNState sourceState, ATNState targetState) {
		if (targetState == null)
			targetState = atn.createState();

		ATNState exprStartState = sourceState;
		ATNState exprEndState = atn.createState();

		atn.createEpsilonEdge(exprEndState, targetState);

		expr.appendToATN(atn, exprStartState, exprEndState);

		switch (multiplicity) {
		case ONE:
			break;

		case OPTIONAL:
			atn.createEpsilonEdge(exprStartState, exprEndState);
			break;

		case ANY:
			atn.createEpsilonEdge(exprStartState, exprEndState);
			atn.createEpsilonEdge(exprEndState, exprStartState);
			break;

		case ONEORMORE:
			atn.createEpsilonEdge(exprEndState, exprStartState);
			break;
		}

		return targetState;
	}
}
