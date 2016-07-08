package com.kilic.kmeta.core.dfa;

import com.kilic.kmeta.core.stream.IStream;

public interface IDFAEdge {
	PredictionDFAState getFrom();
	PredictionDFAState getTo();
	
	boolean move(IStream input);
}
