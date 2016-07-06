package com.kilic.kmeta.core.atn;

import com.kilic.kmeta.core.stream.IStream;

public interface IATNEdge {
	ATNState getFrom();
	ATNState getTo();
	
	String getLabel();
	boolean move(IStream input);
}
