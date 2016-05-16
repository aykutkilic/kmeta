package com.kilic.kmeta.core.discriminator;

import java.util.HashSet;
import java.util.Set;

public class CharSet {
	Set<CharRange> ranges = new HashSet<>();

	public static CharSet LETTER = new CharSet().addRange(new CharRange('a', 'z'))
												.addRange(new CharRange('A', 'Z'));
	
	public static CharSet DEC = new CharSet().addRange(new CharRange('0', '9'));
	
	public static CharSet HEX = new CharSet().addRange(new CharRange('0', '9'))
											 .addRange(new CharRange('a', 'f'))
											 .addRange(new CharRange('A', 'F'));
	
	public CharSet() {}

	public CharSet addRange(CharRange range) {
		ranges.add(range);
		simplifyRanges();
		return this;
	}

	public CharSet addSingleton(char singleton) {
		ranges.add(new CharRange(singleton));
		simplifyRanges();
		return this;
	}

	private void simplifyRanges() {
		do {
			for(CharRange r1 : ranges) {
				for(CharRange r2 : ranges) {
					if(r1.isUnifiable(r2)) {
						unify(r1,r2);
						continue;
					}
				}
			}
			
			break;
		} while(true);
	}

	private void unify(CharRange r1, CharRange r2) {
		ranges.remove(r1);
		ranges.remove(r2);
		CharRange unification = r1.getUnification(r2);
		assert(unification!=null);
		ranges.add(unification);
	}

	public CharSet getUnion(CharSet b) {
		return null;
	}

	public CharSet getInstersection(CharSet b) {
		return null;
	}

	public CharSet getSubtraction(CharSet b) {
		return null;
	}
	
	public boolean containsSingleton(char singleton) {
		for(CharRange r : ranges) {
			if(r.containsSingleton(singleton)) return true;
		}
		
		return false;
	}
}
