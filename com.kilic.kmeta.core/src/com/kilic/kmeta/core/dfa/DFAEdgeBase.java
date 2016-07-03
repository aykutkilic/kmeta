package com.kilic.kmeta.core.dfa;

public abstract class DFAEdgeBase implements IDFAEdge {
	DFAState from, to;
	
	@Override
	public DFAState getFrom() {
		return from;
	}
	
	@Override
	public DFAState getTo() {
		return to;
	}
}
