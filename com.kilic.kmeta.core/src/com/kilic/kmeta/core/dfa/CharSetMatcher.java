package com.kilic.kmeta.core.dfa;

import com.kilic.kmeta.core.discriminator.CharSet;
import com.kilic.kmeta.core.stream.IStream;

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
		return charSetToMatch.containsSingleton(stream.getChar());
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
