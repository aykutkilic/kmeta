package com.kilic.kmeta.core.syntax;

import com.kilic.kmeta.core.atn.ATN;
import com.kilic.kmeta.core.atn.IATNState;
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
	public IATNState appendToATN(ATN atn, IATNState sourceState, IATNState targetState) {
		if (targetState == null)
			targetState = atn.createRegularState();

		IATNState exprStartState = null;
		IATNState exprEndState = atn.createRegularState();

		switch (multiplicity) {
		case ONE:
			exprStartState = sourceState;
			atn.createEpsilonEdge(exprEndState, targetState);
			break;

		case OPTIONAL:
			exprStartState = atn.createDecisionState();
			atn.createEpsilonEdge(sourceState, exprStartState);
			atn.createEpsilonEdge(exprStartState, exprEndState);
			atn.createEpsilonEdge(exprEndState, targetState);
			break;

		case ANY:
			exprStartState = atn.createDecisionState();
			atn.createEpsilonEdge(sourceState, exprStartState);
			atn.createEpsilonEdge(exprStartState, exprEndState);
			atn.createEpsilonEdge(exprEndState, exprStartState);
			break;

		case ONEORMORE:
			exprStartState = atn.createDecisionState();
			atn.createEpsilonEdge(sourceState, exprStartState);
			atn.createEpsilonEdge(exprEndState, exprStartState);
			break;
		}
		
		assert(exprStartState!=null);
		expr.appendToATN(atn, exprStartState, exprEndState);

		return targetState;
	}
}
