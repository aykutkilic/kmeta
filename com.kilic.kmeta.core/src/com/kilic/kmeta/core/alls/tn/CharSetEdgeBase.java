package com.kilic.kmeta.core.alls.tn;

import com.kilic.kmeta.core.alls.stream.IStream;
import com.kilic.kmeta.core.util.CharSet;

public abstract class CharSetEdgeBase<S extends IState<?, ?>> extends EdgeBase<S> {
	private final CharSet charSet;

	protected CharSetEdgeBase(final S from, final S to, final CharSet charSet) {
		this.charSet = charSet;
		connect(from, to);
	}

	@Override
	public String getLabel() {
		return charSet.toString();
	}

	public CharSet getCharSet() {
		return charSet;
	}

	@Override
	public String match(final IStream input) {
		char c;
		return charSet.containsSingleton(c = input.lookAheadChar(0)) ? String.valueOf(c) : null;
	}

	@Override
	public int hashCode() {
		return charSet.hashCode();
	}

	@Override
	public boolean equals(final Object other) {
		if (this == other)
			return true;

		if (!(other instanceof CharSetEdgeBase<?>))
			return false;

		return charSet.equals(((CharSetEdgeBase<?>) other).charSet);
	}
}
