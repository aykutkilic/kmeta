package com.kilic.kmeta.core.syntax;

public class SeparatorExpr implements ISyntaxExpr {
	String separator;
	ISyntaxExpr expr;
	
	public String getSeparator() { return separator; }
	public void setSeparator(String separator) { this.separator = separator; }
	
	public ISyntaxExpr getExpr() { return expr; }
	public void setExpr(ISyntaxExpr expr) { this.expr = expr; }
}
