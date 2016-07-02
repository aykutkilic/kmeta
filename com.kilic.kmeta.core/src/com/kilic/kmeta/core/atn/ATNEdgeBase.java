package com.kilic.kmeta.core.atn;

public abstract class ATNEdgeBase implements IATNEdge {
	IATNState from, to;
	
	@Override
	public IATNState getFrom() {
		return from;
	}
	
	@Override
	public IATNState getTo() {
		return to;
	}
}
