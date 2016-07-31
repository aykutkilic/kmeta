package com.kilic.kmeta.core.tests.expr;

public class DecL implements Expr {
	public String val;

	@Override
	public long eval() {
		return Long.parseLong(val);
	}
}
