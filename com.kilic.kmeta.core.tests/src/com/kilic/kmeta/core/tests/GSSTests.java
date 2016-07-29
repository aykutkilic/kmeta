package com.kilic.kmeta.core.tests;

import org.junit.Test;

import com.kilic.kmeta.core.alls.analysis.GSS;

public class GSSTests {
	@Test
	public void test() {
		GSS gss = new GSS();

		for (int i = 1; i <= 5; i++)
			gss.merge(i);

		gss.toGraphviz();
		gss.move(1, 11);
		gss.move(2, 12);
		gss.call(11, 21, 31);
		gss.call(12, 22, 32);
		gss.call(3, 23, 31);
		gss.call(4, 24, 31);
		gss.call(5, 25, 32);
		gss.call(31, 41, 50);
		gss.call(32, 42, 50);
		gss.toGraphviz();
		gss.returnFromCall(50);
		gss.returnFromCall(41);
		gss.move(42, 43);
		gss.returnFromCall(43);
		gss.returnFromCall(21);
		gss.toGraphviz();
	}
}
