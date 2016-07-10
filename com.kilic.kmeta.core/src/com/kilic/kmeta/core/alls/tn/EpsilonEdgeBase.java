package com.kilic.kmeta.core.alls.tn;

import com.kilic.kmeta.core.alls.stream.IStream;

public class EpsilonEdgeBase <S extends IState<?,?>> extends EdgeBase<S> {
	@Override
	public String getLabel() {
		return "epsilon";
	}

	@Override
	public boolean moves(IStream input) {
		return false;
	}
}
