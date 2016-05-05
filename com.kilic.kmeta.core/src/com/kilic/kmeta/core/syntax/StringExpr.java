package com.kilic.kmeta.core.syntax;

public class StringExpr implements ISyntaxExpr {
	String string;
	
	public StringExpr(String string) { this.string = string; }
	
	public String getString() { return string; }
	public void setString(String string) { this.string = string; }
}
