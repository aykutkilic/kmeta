package com.kilic.kmeta.core.syntax;

public class CharRangeExpr implements ISyntaxExpr {
	int startChar;
	int endChar;
	
	public CharRangeExpr( int startChar, int endChar ) {
		this.startChar = startChar;
		this.endChar = endChar;
	}
	
	public int getStartChar() { return startChar; }
	public void setStartChar(int startChar) { this.startChar = startChar; }
	
	public int getEndChar() { return endChar; }
	public void setEndChar(int endChar) { this.endChar = endChar; }
}
