package com.kilic.kmeta.core.alls.tn;

import com.kilic.kmeta.core.alls.stream.IStream;

public interface IEdge<S extends IState<?,?>> extends ILabeled {
	S getFrom();
	void setFrom(final S state);
	S getTo();
	void setTo(final S state);

	void connect(final S from, final S to);
	void disconnect();
	
	String match(final IStream input);
}
