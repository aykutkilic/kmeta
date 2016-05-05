package com.kilic.kmeta.core.syntax;

import com.kilic.kmeta.core.meta.IMultiplicity;

public class MultiplicityExpr implements ISyntaxExpr {
	ISyntaxExpr expr;
	IMultiplicity multiplicity;
	
	public MultiplicityExpr( IMultiplicity multiplicity, ISyntaxExpr expr ) {
		this.multiplicity = multiplicity;
		this.expr = expr;
	}
	
	public ISyntaxExpr getExpr() { return expr; }
	public void setExpr(ISyntaxExpr expr) { this.expr = expr; }
	
	public IMultiplicity getMultiplicity() { return multiplicity; }
	public void setMultiplicity(IMultiplicity mult) { this.multiplicity = mult; }
}
