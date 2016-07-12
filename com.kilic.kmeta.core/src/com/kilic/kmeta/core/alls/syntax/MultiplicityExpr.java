package com.kilic.kmeta.core.alls.syntax;

import com.kilic.kmeta.core.alls.atn.ATN;
import com.kilic.kmeta.core.alls.atn.ATNState;
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

		ATNState decisionState = null;
		ATNState exprStartState = null;
		//ATNState exprEndState = null;

		switch (multiplicity) {
		case ONE:
			expr.appendToATN(atn, sourceState, targetState);
			break;

		case OPTIONAL:
			decisionState = atn.createState();
			exprStartState = atn.createState();
			atn.createEpsilonEdge(sourceState, decisionState);
			atn.createEpsilonEdge(decisionState, exprStartState);
			expr.appendToATN(atn, exprStartState, targetState);
			atn.createEpsilonEdge(decisionState, targetState);
			break;

		case ANY:
			decisionState = atn.createState();
			exprStartState = atn.createState();
			atn.createEpsilonEdge(sourceState, decisionState);
			atn.createEpsilonEdge(decisionState, exprStartState);
			expr.appendToATN(atn, exprStartState, decisionState);
			atn.createEpsilonEdge(exprStartState, targetState);
			break;

		case ONEORMORE:
			exprStartState = atn.createState();
			decisionState = atn.createState();
			atn.createEpsilonEdge(sourceState, exprStartState);
			expr.appendToATN(atn, exprStartState, decisionState);
			atn.createEpsilonEdge(decisionState, exprStartState);
			atn.createEpsilonEdge(decisionState, targetState);
			break;
		}

		return targetState;
	}
}
