package com.kilic.kmeta.core.tests;

import org.junit.Test;

import com.kilic.kmeta.core.alls.stream.IStream;
import com.kilic.kmeta.core.alls.stream.StringStream;

public class HandWrittenParserTests {
	@Test
	public void testParser() {
		IStream s = new StringStream("-poipoi+((G+3)*;0x15-6*8.2e+10+0xF;F;");
		HandWrittenParser p = new HandWrittenParser(s);
		p.parse();
	}
}
