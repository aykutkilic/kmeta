package com.kilic.kmeta.core.syntax;

import com.kilic.kmeta.core.alls.atn.ATN;
import com.kilic.kmeta.core.alls.atn.ATNState;
import com.kilic.kmeta.core.meta.Multiplicity;

public class DelimiterExpr implements ISyntaxExpr {
	private String delimiter;
	private ISyntaxExpr expr;
	private Multiplicity multiplicity;

	public DelimiterExpr(final ISyntaxExpr expr, final String delimiter, final Multiplicity multiplicity) {
		assert (multiplicity == Multiplicity.ANY || multiplicity == Multiplicity.ONEORMORE);

		this.expr = expr;
		this.delimiter = delimiter;
		this.multiplicity = multiplicity;
	}

	public String getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(final String delimiter) {
		this.delimiter = delimiter;
	}

	public ISyntaxExpr getExpr() {
		return expr;
	}

	public void setExpr(final ISyntaxExpr expr) {
		this.expr = expr;
	}

	@Override
	public ATNState appendToATN(final ATN atn, final ATNState sourceState, ATNState targetState) {
		if (targetState == null)
			targetState = atn.createState();

		final ATNState exprStartState = atn.createState();
		final ATNState exprEndState = atn.createState();
		final ATNState preDelimState = atn.createState();
		final ATNState afterDelimState = atn.createState();

		atn.createEpsilonEdge(sourceState, exprStartState);
		atn.createEpsilonEdge(exprEndState, targetState);

		if (multiplicity == Multiplicity.ANY)
			atn.createEpsilonEdge(exprStartState, targetState);

		expr.appendToATN(atn, exprStartState, exprEndState);
		atn.createEpsilonEdge(exprEndState, preDelimState);
		atn.createEdgeFromString(preDelimState, afterDelimState, delimiter);
		expr.appendToATN(atn, afterDelimState, exprEndState);

		return targetState;
	}
}
