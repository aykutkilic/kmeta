package com.kilic.kmeta.core.syntax;

import com.kilic.kmeta.core.atn.ATN;
import com.kilic.kmeta.core.atn.ATNState;

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
	public ATNState appendToATN(ATN atn, ATNState sourceState, ATNState targetState) {
		if (targetState == null)
			targetState = atn.createState();

		ATNState exprStartState = atn.createState();
		ATNState exprEndState = atn.createState();
		ATNState afterSepState = atn.createState();

		atn.createEpsilonEdge(sourceState, exprStartState);
		atn.createEpsilonEdge(exprEndState, targetState);

		atn.createEpsilonEdge(exprStartState, targetState);

		expr.appendToATN(atn, exprStartState, exprEndState);
		atn.createStringEdge(exprEndState, afterSepState, separator);
		expr.appendToATN(atn, afterSepState, exprEndState);

		return targetState;
	}
}
