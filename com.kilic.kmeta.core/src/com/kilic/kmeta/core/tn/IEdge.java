package com.kilic.kmeta.core.tn;

import com.kilic.kmeta.core.stream.IStream;

public interface IEdge<K> extends ILabeled {
	IState<K> getFrom();
	IState<K> getTo();

	boolean moves(IStream input);
}
