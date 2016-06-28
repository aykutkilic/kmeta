package com.kilic.kmeta.core.dfa;

import com.kilic.kmeta.core.stream.IStream;

public class StringMatcher implements IMatcher {
	String stringToMatch;

	public StringMatcher(String stringToMatch) {
		assert (!stringToMatch.isEmpty());
		this.stringToMatch = stringToMatch;
	}

	public String getString() {
		return stringToMatch;
	}

	@Override
	public boolean match(IStream stream) {
		String string = stream.lookAheadString(0, stringToMatch.length());
		return string.equals(stringToMatch);
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof StringMatcher)
			return stringToMatch.equals(((StringMatcher) other).stringToMatch);

		return false;
	}

	@Override
	public String toString() {
		return stringToMatch;
	}
}
