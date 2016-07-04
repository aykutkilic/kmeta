package com.kilic.kmeta.core.syntax;

import com.kilic.kmeta.core.atn.ATN;
import com.kilic.kmeta.core.atn.ATNState;
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

		ATNState exprStartState = null;
		ATNState exprEndState = null;

		switch (multiplicity) {
		case ONE:
			expr.appendToATN(atn, sourceState, targetState);
			break;

		case OPTIONAL:
			exprStartState = atn.createState();
			atn.createEpsilonEdge(sourceState, exprStartState);
			expr.appendToATN(atn, exprStartState, targetState);
			atn.createEpsilonEdge(exprStartState, targetState);
			break;

		case ANY:
			exprStartState = atn.createState();
			exprEndState = atn.createState();
			atn.createEpsilonEdge(sourceState, exprStartState);
			expr.appendToATN(atn, exprStartState, exprEndState);
			atn.createEpsilonEdge(exprEndState, exprStartState);
			atn.createEpsilonEdge(exprStartState, targetState);
			break;

		case ONEORMORE:
			exprStartState = atn.createState();
			exprEndState = atn.createState();
			atn.createEpsilonEdge(sourceState, exprStartState);
			expr.appendToATN(atn, exprStartState, exprEndState);
			atn.createEpsilonEdge(exprEndState, exprStartState);
			atn.createEpsilonEdge(exprEndState, targetState);
			break;
		}

		return targetState;
	}
}
