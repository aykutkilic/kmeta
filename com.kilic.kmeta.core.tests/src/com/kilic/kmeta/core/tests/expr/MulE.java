package com.kilic.kmeta.core.tests.expr;

public class MulE extends BinE {

	@Override
	public long eval() {
		if(op.equals("*"))
			return left.eval() * right.eval();
		
		if(op.equals("/"))
			return left.eval() / right.eval();
		
		return 0;

	}

}
