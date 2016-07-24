package com.kilic.kmeta.core.util;

import java.util.ArrayList;
import java.util.List;

public class CharRange {
	char start;
	char end;
	boolean isEmpty;

	public static CharRange EMPTY = new CharRange();

	public CharRange() {
		isEmpty = true;
	}

	public CharRange(char start, char end) {
		isEmpty = false;
		if (end < start) {
			this.start = end;
			this.end = start;
		} else {
			this.start = start;
			this.end = end;
		}
	}

	public CharRange(char singleton) {
		this(singleton, singleton);
	}

	public char getStart() {
		return start;
	}

	public char getEnd() {
		return end;
	}

	public boolean isEmpty() {
		return isEmpty;
	}

	public boolean isSingleton() {
		return !isEmpty() && start == end;
	}

	public char getSingleton() {
		return start;
	}

	public CharRange getIntersection(CharRange other) {
		if (isEmpty() || other.isEmpty())
			return EMPTY;

		char istart = start > other.start ? start : other.start;
		char iend = end < other.end ? end : other.end;

		if (istart > iend)
			return EMPTY;

		return new CharRange(istart, iend);
	}

	public List<CharRange> getSubtraction(CharRange other) {
		List<CharRange> result = new ArrayList<>();
		if (isEmpty() || other.isEmpty())
			return result;

		CharRange i = getIntersection(other);

		if (i.isEmpty()) {
			result.add(this);
			return result;
		}

		if (i.start > this.start)
			result.add(new CharRange(start, (char) (i.start - 1)));

		if (i.end < this.end)
			result.add(new CharRange((char) (i.end + 1), end));

		return result;
	}

	public boolean contains(CharRange other) {
		if (other.isEmpty())
			return true;
		if (isEmpty() && !other.isEmpty())
			return false;

		return start <= other.start && end >= other.end;
	}

	/**
	 * this range at least has 1 element more than the subset.
	 */
	public boolean properContains(CharRange other) {
		if (other.isEmpty())
			return true;
		if (isEmpty() && !other.isEmpty())
			return false;

		return (start < other.start && end >= other.end) || (start <= other.start && end > other.end);
	}

	public boolean containsSingleton(char value) {
		return start <= value && end >= value;
	}

	public boolean isAdjacent(char singleton) {
		return singleton == start - 1 || singleton == end + 1;
	}

	public boolean isAdjacent(CharRange other) {
		return other.end == start - 1 || other.start == end + 1;
	}

	public CharRange getUnification(CharRange other) {
		char ustart = start < other.start ? start : other.start;
		char uend = end > other.end ? end : other.end;

		return new CharRange(ustart, uend);
	}

	public boolean isUnifiable(CharRange other) {
		return isAdjacent(other) || !getIntersection(other).isEmpty();
	}

	@Override
	public int hashCode() {
		return start * 0x10000 + end;
	}

	/**
	 * Please note that empty set is not a member of empty set.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CharRange) {
			CharRange other = (CharRange) obj;
			if (isEmpty() || other.isEmpty())
				return false;
			return start == other.start && end == other.end;
		}

		return false;
	}

	@Override
	public String toString() {
		if (isEmpty())
			return new String(new char[] { 238 });

		if (isSingleton())
			return new String(new char[] { safeChar(start) });

		if (start == 0x00 && end == 0xFF)
			return "any";

		return new String(new char[] { safeChar(start), '-', safeChar(end) });
	}

	private char safeChar(char input) {
		if (input < 0x20 || input > 253)
			return '?';

		return input;
	}
}
