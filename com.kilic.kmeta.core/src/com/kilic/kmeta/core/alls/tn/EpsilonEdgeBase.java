package com.kilic.kmeta.core.alls.tn;

import com.kilic.kmeta.core.alls.stream.IStream;

public class EpsilonEdgeBase<S extends IState<?, ?>> extends EdgeBase<S> {
	protected EpsilonEdgeBase(final S from, final S to) {
		connect(from, to);
	}
	
	@Override
	public String getLabel() {
		return new String("eps");
	}

	@Override
	public String match(final IStream input) {
		return null;
	}
}
