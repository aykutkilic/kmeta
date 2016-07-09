package com.kilic.kmeta.core.alls.tn;

import com.kilic.kmeta.core.alls.stream.IStream;

public interface IEdge<K> extends ILabeled {
	IState<K> getFrom();
	IState<K> getTo();

	boolean moves(IStream input);
}
