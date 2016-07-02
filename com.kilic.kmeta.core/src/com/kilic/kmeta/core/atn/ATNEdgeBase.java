package com.kilic.kmeta.core.atn;

public abstract class ATNEdgeBase implements IATNEdge {
	ATNState from, to;
	
	@Override
	public ATNState getFrom() {
		return from;
	}
	
	@Override
	public ATNState getTo() {
		return to;
	}
}
