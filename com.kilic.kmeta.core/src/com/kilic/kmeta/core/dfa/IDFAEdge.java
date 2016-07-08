package com.kilic.kmeta.core.dfa;

import com.kilic.kmeta.core.stream.IStream;

public interface IDFAEdge<SK> {
	IDFAState<SK> getFrom();
	IDFAState<SK> getTo();
	String getLabel();
	
	boolean move(IStream input);
}
