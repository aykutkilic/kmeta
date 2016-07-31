package com.kilic.kmeta.core.tests.expr;

public class HexL implements Expr {
	public String val;

	@Override
	public long eval() {
		return Long.parseLong(val.substring(0, val.length() - 1), 16);
	}
}
