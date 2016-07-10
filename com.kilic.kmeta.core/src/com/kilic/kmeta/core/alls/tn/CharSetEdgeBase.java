package com.kilic.kmeta.core.alls.tn;

import com.kilic.kmeta.core.alls.stream.IStream;
import com.kilic.kmeta.core.util.CharSet;

public abstract class CharSetEdgeBase <S extends IState<?,?>> extends EdgeBase<S> {
	CharSet charSet;

	protected CharSetEdgeBase(CharSet charSet) {
		this.charSet = charSet;
	}

	@Override
	public String getLabel() {
		return charSet.toString();
	}

	@Override
	public boolean moves(IStream input) {
		char c = input.lookAheadChar(0);
		return charSet.containsSingleton(c);
	}
}
