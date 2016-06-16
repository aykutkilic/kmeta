package com.kilic.kmeta.core.automaton;

import com.kilic.kmeta.core.stream.IStream;
import com.kilic.kmeta.core.util.CharSet;

public class CharSetMatcher implements IMatcher {
	CharSet charSetToMatch;

	public CharSetMatcher(CharSet charSetToMatch) {
		this.charSetToMatch = charSetToMatch;
	}

	public CharSet getCharSet() {
		return charSetToMatch;
	}

	@Override
	public boolean match(IStream stream) {
		return charSetToMatch.containsSingleton(stream.lookAheadChar());
	}
	
	@Override
	public int hashCode() {
		return charSetToMatch.hashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof CharSetMatcher)
			return charSetToMatch.equals(((CharSetMatcher) other).charSetToMatch);

		return false;
	}

	@Override
	public String toString() {
		return charSetToMatch.toString();
	}
}
