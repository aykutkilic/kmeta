package com.kilic.kmeta.core.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CharSet {
	private List<CharRange> ranges = new ArrayList<>();

	public static CharSet LETTER = new CharSet().addRange(new CharRange('a', 'z')).addRange(new CharRange('A', 'Z'));
	public static CharSet DEC = new CharSet().addRange(new CharRange('0', '9'));
	public static CharSet HEX = new CharSet().addRange(new CharRange('0', '9')).addRange(new CharRange('a', 'f'))
			.addRange(new CharRange('A', 'F'));

	public static CharSet ANY = new CharSet().addRange(new CharRange((char) 0x00, (char) 0xFF));

	public CharSet() {
	}

	public CharSet addRange(final CharRange range) {
		ranges.add(range);
		simplifyRanges();
		return this;
	}

	public CharSet addSingleton(final char... singletons) {
		for (final char singleton : singletons)
			ranges.add(new CharRange(singleton));

		simplifyRanges();
		return this;
	}

	public boolean isEmpty() {
		for (final CharRange r : ranges)
			if (!r.isEmpty())
				return false;

		return true;
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
				final CharRange r1 = ranges.get(i);
				final CharRange r2 = ranges.get(j);

				if (r1.isUnifiable(r2)) {
					unify(r1, r2);
					return true;
				}
			}
		}

		return false;
	}

	private void unify(final CharRange r1, final CharRange r2) {
		ranges.remove(r1);
		ranges.remove(r2);
		final CharRange unification = r1.getUnification(r2);
		assert (unification != null);
		ranges.add(unification);
	}

	public CharSet getUnion(final CharSet b) {
		final CharSet result = new CharSet();
		result.ranges.addAll(this.ranges);
		result.ranges.addAll(b.ranges);
		result.simplifyRanges();
		return result;
	}

	public CharSet getInstersection(final CharSet b) {
		final CharSet result = new CharSet();

		for (final CharRange ra : ranges) {
			for (final CharRange rb : b.ranges) {
				final CharRange i = ra.getIntersection(rb);
				if (!i.isEmpty())
					result.addRange(i);
			}
		}

		return result;
	}

	public CharSet getSubtraction(final CharSet b) {
		final CharSet result = new CharSet();

		for (final CharRange ra : ranges) {
			List<CharRange> remainder = new ArrayList<>();
			remainder.add(ra);
			for (final CharRange rb : b.ranges) {
				final List<CharRange> newRemainder = new ArrayList<>();
				for (final CharRange r : remainder)
					newRemainder.addAll(r.getSubtraction(rb));
				remainder = newRemainder;
			}

			for (final CharRange r : remainder)
				result.addRange(r);
		}

		return result;
	}

	public boolean intersects(final CharSet b) {
		for (final CharRange ra : ranges) {
			for (final CharRange rb : b.ranges) {
				final CharRange i = ra.getIntersection(rb);
				if (!i.isEmpty())
					return true;
			}
		}

		return false;
	}

	public boolean containsSingleton(final char singleton) {
		for (final CharRange r : ranges) {
			if (r.containsSingleton(singleton))
				return true;
		}

		return false;
	}

	public static Set<CharSet> getDistinctCharSets(final Set<CharSet> charSets) {
		final Set<CharSet> result = new HashSet<>();

		for (final CharSet cs : charSets) {
			if (result.isEmpty()) {
				result.add(cs);
				continue;
			}

			for (final CharSet dcs : new HashSet<>(result)) {
				if (dcs.equals(cs))
					continue;

				final CharSet intersection = dcs.getInstersection(cs);

				if (intersection.isEmpty()) {
					result.add(cs);
				} else {
					result.remove(dcs);

					final CharSet dcsMinusCs = dcs.getSubtraction(cs);
					if (!dcsMinusCs.isEmpty())
						result.add(dcsMinusCs);

					result.add(intersection);
					result.add(cs.getSubtraction(dcs));
				}
			}
		}

		return result;
	}

	@Override
	public int hashCode() {
		return this.ranges.hashCode();
	}

	@Override
	public boolean equals(final Object other) {
		if (other instanceof CharSet)
			return this.ranges.equals(((CharSet) other).ranges);
		return false;
	}

	@Override
	public String toString() {
		final StringBuilder result = new StringBuilder();
		result.append('[');
		for (final CharRange r : ranges)
			result.append(r.toString());
		result.append(']');
		return result.toString();
	}
}
