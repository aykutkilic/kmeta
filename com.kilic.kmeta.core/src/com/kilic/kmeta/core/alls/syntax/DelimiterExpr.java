package com.kilic.kmeta.core.alls.syntax;

import com.kilic.kmeta.core.alls.atn.ATN;
import com.kilic.kmeta.core.alls.atn.ATNState;
import com.kilic.kmeta.core.meta.Multiplicity;

public class DelimiterExpr implements ISyntaxExpr {
	String delimiter;
	ISyntaxExpr expr;
	Multiplicity multiplicity;

	public DelimiterExpr(ISyntaxExpr expr, String delimiter, Multiplicity multiplicity) {
		assert (multiplicity == Multiplicity.ANY || multiplicity == Multiplicity.ONEORMORE);

		this.expr = expr;
		this.delimiter = delimiter;
		this.multiplicity = multiplicity;
	}

	public String getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	public ISyntaxExpr getExpr() {
		return expr;
	}

	public void setExpr(ISyntaxExpr expr) {
		this.expr = expr;
	}

	@Override
	public ATNState appendToATN(ATN atn, ATNState sourceState, ATNState targetState) {
		if (targetState == null)
			targetState = atn.createState();

		ATNState exprStartState = atn.createState();
		ATNState exprEndState = atn.createState();
		ATNState preDelimState = atn.createState();
		ATNState afterDelimState = atn.createState();

		atn.createEpsilonEdge(sourceState, exprStartState);
		atn.createEpsilonEdge(exprEndState, targetState);

		if (multiplicity == Multiplicity.ANY)
			atn.createEpsilonEdge(exprStartState, targetState);

		expr.appendToATN(atn, exprStartState, exprEndState);
		atn.createEpsilonEdge(exprEndState, preDelimState);
		atn.createStringEdge(preDelimState, afterDelimState, delimiter);
		expr.appendToATN(atn, afterDelimState, exprEndState);

		return targetState;
	}
}
