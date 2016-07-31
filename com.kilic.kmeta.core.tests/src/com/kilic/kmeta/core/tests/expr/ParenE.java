package com.kilic.kmeta.core.tests.expr;

public class ParenE implements Expr {
	public Expr expr;

	@Override
	public long eval() {
		return expr.eval();
	}
}
