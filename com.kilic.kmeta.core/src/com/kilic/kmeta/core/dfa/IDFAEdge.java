package com.kilic.kmeta.core.dfa;

import com.kilic.kmeta.core.stream.IStream;

public interface IDFAEdge {
	DFAState getFrom();
	DFAState getTo();
	
	boolean move(IStream input);
}
