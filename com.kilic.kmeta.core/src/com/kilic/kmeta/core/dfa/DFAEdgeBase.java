package com.kilic.kmeta.core.dfa;

public abstract class DFAEdgeBase<SK> implements IDFAEdge<SK> {
	IDFAState<SK> from, to;
	
	@Override
	public IDFAState<SK> getFrom() {
		return from;
	}
	
	@Override
	public IDFAState<SK> getTo() {
		return to;
	}
}
