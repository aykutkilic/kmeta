package com.kilic.kmeta.core.tests;

import org.junit.Test;

import com.kilic.kmeta.core.discriminator.CharRange;
import com.kilic.kmeta.core.discriminator.CharSet;

public class CharSetTests {
	@Test
	public void charSetTests() {
		System.out.println(CharSet.DEC.toString());
		System.out.println(CharSet.HEX);
		System.out.println(CharSet.LETTER);
		
		CharSet s1 = new CharSet();
		
		s1.addRange(new CharRange('.'));
		s1.addRange(new CharRange('0','8'));
		s1.addRange(new CharRange('9'));
		
		System.out.println(s1);
	}
}
