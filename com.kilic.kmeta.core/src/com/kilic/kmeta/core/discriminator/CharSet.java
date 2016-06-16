package com.kilic.kmeta.core.discriminator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CharSet {
	List<CharRange> ranges = new ArrayList<>();

	public static CharSet LETTER = new CharSet().addRange(new CharRange('a', 'z')).addRange(new CharRange('A', 'Z'));
	public static CharSet DEC = new CharSet().addRange(new CharRange('0', '9'));
	public static CharSet HEX = new CharSet().addRange(new CharRange('0', '9')).addRange(new CharRange('a', 'f'))
			.addRange(new CharRange('A', 'F'));

	public CharSet() {
	}

	public CharSet addRange(CharRange range) {
		ranges.add(range);
		simplifyRanges();
		return this;
	}

	public CharSet addSingleton(char... singletons) {
		for( char singleton : singletons )
			ranges.add(new CharRange(singleton));
		
		simplifyRanges();
		return this;
	}

	private void simplifyRanges() {
		while (unifyNext())
			;

		Collections.sort(ranges, new Comparator<CharRange>() {

			@Override
			public int compare(CharRange cr1, CharRange cr2) {
				if (cr1.getStart() < cr2.getStart())
					return -1;
				if (cr1.getStart() > cr2.getStart())
					return 1;

				return 0;
			}

		});
	}

	private boolean unifyNext() {
		for (int i = 0; i < ranges.size(); i++) {
			for (int j = i + 1; j < ranges.size(); j++) {
				CharRange r1 = ranges.get(i);
				CharRange r2 = ranges.get(j);

				if (r1.isUnifiable(r2)) {
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
		assert (unification != null);
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
		CharSet result = new CharSet();

		for (CharRange ra : ranges) {
			for (CharRange rb : b.ranges) {
				CharRange i = ra.getIntersection(rb);
				if (!i.isEmpty())
					result.addRange(i);
			}
		}

		return result;
	}

	public CharSet getSubtraction(CharSet b) {
		CharSet result = new CharSet();

		for (CharRange ra : ranges) {
			List<CharRange> remainder = new ArrayList<>();
			remainder.add(ra);
			for (CharRange rb : b.ranges) {
				List<CharRange> newRemainder = new ArrayList<>();
				for (CharRange r : remainder)
					newRemainder.addAll(r.getSubtraction(rb));
				remainder = newRemainder;
			}

			for (CharRange r : remainder)
				result.addRange(r);
		}

		return result;
	}

	public boolean intersects(CharSet b) {
		for (CharRange ra : ranges) {
			for (CharRange rb : b.ranges) {
				CharRange i = ra.getIntersection(rb);
				if (!i.isEmpty())
					return true;
			}
		}

		return false;
	}
	
	public boolean containsSingleton(char singleton) {
		for (CharRange r : ranges) {
			if (r.containsSingleton(singleton))
				return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		return this.ranges.hashCode();
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof CharSet)
			return this.ranges.equals(((CharSet) other).ranges);
		return false;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append('[');
		for (CharRange r : ranges)
			result.append(r.toString());
		result.append(']');
		return result.toString();
	}
}
