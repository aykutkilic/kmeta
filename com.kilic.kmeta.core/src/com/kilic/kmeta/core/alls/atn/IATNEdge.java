package com.kilic.kmeta.core.alls.atn;

import com.kilic.kmeta.core.alls.stream.IStream;

public interface IATNEdge {
	ATNState getFrom();
	ATNState getTo();
	
	String getLabel();
	boolean move(IStream input);
}
