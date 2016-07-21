package com.kilic.kmeta.core.alls.tn;

import com.kilic.kmeta.core.alls.stream.IStream;

public class EpsilonEdgeBase<S extends IState<?, ?>> extends EdgeBase<S> {
	protected EpsilonEdgeBase(S from, S to) {
		connect(from, to);
	}
	
	@Override
	public String getLabel() {
		return new String("eps");
	}

	@Override
	public String match(IStream input) {
		return null;
	}
}
