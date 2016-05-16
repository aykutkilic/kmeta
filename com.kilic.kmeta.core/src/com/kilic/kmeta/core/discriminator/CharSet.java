package com.kilic.kmeta.core.discriminator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CharSet {
	List<CharRange> ranges = new ArrayList<>();

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
		while(unifyNext());
	}
	
	private boolean unifyNext() {
		for(int i=1; i<ranges.size();i++) {
			for(int j=i+1; j<ranges.size(); j++) {
				CharRange r1 = ranges.get(i);
				CharRange r2 = ranges.get(j);

				if( r1.isUnifiable(r2)){
					unify(r1, r2);
					return true;
				}
			}
		}
		
		return false;
	}

	private void unify(CharRange r1, CharRange r2) {
		ranges.remove(r1);
		ranges.remove(r2);
		CharRange unification = r1.getUnification(r2);
		assert(unification!=null);
		ranges.add(unification);
	}

	public CharSet getUnion(CharSet b) {
		CharSet result = new CharSet();
		result.ranges.addAll(this.ranges);
		result.ranges.addAll(b.ranges);
		result.simplifyRanges();
		return result;
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
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append('[');
		for(CharRange r : ranges)
			result.append(r.toString());
		result.append(']');
		return result.toString();
	}
}
