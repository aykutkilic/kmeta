package com.kilic.kmeta.core.dfa;

public abstract class DFAEdgeBase implements IDFAEdge {
	PredictionDFAState from, to;
	
	@Override
	public PredictionDFAState getFrom() {
		return from;
	}
	
	@Override
	public PredictionDFAState getTo() {
		return to;
	}
}
