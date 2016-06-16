package com.kilic.kmeta.core.tests;

import org.junit.Test;

import com.kilic.kmeta.core.util.CharRange;

import static org.junit.Assert.*;

public class CharRangeTests {
	@Test
	public void charRangeTest1() {
		char testChar = 'b';
		CharRange r1 = new CharRange(testChar);
		CharRange r2 = new CharRange(testChar);
		CharRange i = r1.getIntersection(r2);

		assertTrue(r1.equals(r2));
		assertTrue(r2.equals(r1));
		
		assertEquals(testChar, i.getStart());
		assertEquals(testChar, i.getEnd());
		
		assertTrue(i.isSingleton());
		assertEquals(testChar, i.getSingleton());
		
		assertTrue(i.isAdjacent('a'));
		assertTrue(i.isAdjacent('c'));
		
		CharRange u1 = i.getUnification(new CharRange('a'));
		assertTrue(u1.equals(new CharRange('a','b')));
		
		CharRange u2 = u1.getUnification(new CharRange('c'));
		assertTrue(u2.equals(new CharRange('a','c')));
		
		CharRange u3 = u2.getUnification(new CharRange('c'));
		assertTrue(u3.equals(new CharRange('a','c')));
		
		assertTrue(u3.contains(new CharRange('a','c')));
		assertTrue(u3.properContains(new CharRange('a','b')));
		assertTrue(u3.properContains(new CharRange('b','c')));
		
		assertTrue(!u3.contains(new CharRange('b','d')));
		assertTrue(!u3.contains(new CharRange('a','d')));
		
		for(char c = u3.getStart(); c<=u3.getEnd(); c++) {
			CharRange singleton = new CharRange(c);
			assertTrue(u3.getIntersection(singleton).equals(singleton));
		}
		
		CharRange digits = new CharRange('0', '9');
		CharRange subSet = new CharRange('1', '8');
		
		assertTrue(digits.getIntersection(subSet).equals(subSet));
		assertTrue(digits.properContains(subSet));
		assertTrue(digits.contains(subSet));
		
		assertTrue(!subSet.contains(digits));
		assertTrue(!subSet.properContains(digits));
		
		for(char c='0'; c<='5';c++)
			assertTrue(new CharRange('1','4').isUnifiable(new CharRange(c,'9')));
		
		assertTrue(!new CharRange('1','4').isUnifiable(new CharRange('6','9')));
		assertTrue(new CharRange('1','4').getUnification(digits).equals(digits));
	}
}
