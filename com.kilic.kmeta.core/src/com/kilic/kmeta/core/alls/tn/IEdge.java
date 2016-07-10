package com.kilic.kmeta.core.alls.tn;

import com.kilic.kmeta.core.alls.stream.IStream;

public interface IEdge<S extends IState<?,?>> extends ILabeled {
	S getFrom();
	S getTo();

	boolean moves(IStream input);
}
