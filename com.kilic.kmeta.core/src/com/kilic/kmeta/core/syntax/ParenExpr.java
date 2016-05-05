package com.kilic.kmeta.core.syntax;

public class ParenExpr implements ISyntaxExpr {
	ISyntaxExpr expr;
	
	public ISyntaxExpr getExpr() { return expr; }
	public void setExpr(ISyntaxExpr expr) { this.expr = expr; }
}
