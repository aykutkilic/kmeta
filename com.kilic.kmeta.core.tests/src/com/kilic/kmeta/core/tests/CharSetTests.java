package com.kilic.kmeta.core.tests;

import org.junit.Test;

import com.kilic.kmeta.core.discriminator.CharRange;
import com.kilic.kmeta.core.discriminator.CharSet;

public class CharSetTests {
	@Test
	public void charSetTests() {
		System.out.println(CharSet.DEC);
		System.out.println(CharSet.HEX);
		System.out.println(CharSet.LETTER);

		CharSet s1 = new CharSet();

		s1.addRange(new CharRange('.'));
		s1.addRange(new CharRange('c', 'f'));

		for (char c = '9'; c >= '0'; c--)
			s1.addRange(new CharRange(c));

		CharSet s2 = new CharSet();
		s2.addRange(new CharRange('0', '9'));
		s2.addRange(new CharRange('c', 'f'));
		s2.addRange(new CharRange('.'));

		System.out.println(s1.equals(s2));
		System.out.println(s2.equals(s1));

		System.out.println(s1);
		System.out.println(s1.getInstersection(CharSet.HEX));
		System.out.println(CharSet.LETTER.getInstersection(CharSet.HEX));
		System.out.println(CharSet.LETTER.getUnion(CharSet.HEX));
		System.out.println(CharSet.LETTER.getSubtraction(CharSet.HEX));

		System.out.println(new CharSet().addRange(new CharRange('0', '3')).addRange(new CharRange('6', '9'))
				.getSubtraction(new CharSet().addSingleton('2').addSingleton('7')));
	}
}
