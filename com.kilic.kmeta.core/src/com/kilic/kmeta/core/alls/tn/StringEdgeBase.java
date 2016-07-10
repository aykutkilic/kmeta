package com.kilic.kmeta.core.alls.tn;

import com.kilic.kmeta.core.alls.stream.IStream;

public abstract class StringEdgeBase <S extends IState<?,?>> extends EdgeBase<S> {
	String string;
	
	protected StringEdgeBase(String string) {
		this.string = string;
	}

	@Override
	public String getLabel() {
		return string;
	}

	@Override
	public boolean moves(IStream input) {
		return string.equals(input.lookAheadString(0, string.length()));
	}
}
