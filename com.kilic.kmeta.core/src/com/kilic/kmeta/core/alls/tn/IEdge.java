package com.kilic.kmeta.core.alls.tn;

import com.kilic.kmeta.core.alls.stream.IStream;

public interface IEdge<S extends IState<?,?>> extends ILabeled {
	S getFrom();
	void setFrom(S state);
	S getTo();
	void setTo(S state);

	boolean moves(IStream input);
}