package com.kilic.kmeta.core.alls.predictiondfa;

import com.kilic.kmeta.core.alls.discriminatordfa.DiscriminatorDFA;
import com.kilic.kmeta.core.alls.stream.IStream;

public class PredictionDFADiscriminatorEdge extends PredictionDFAEdge {
	DiscriminatorDFA dfa;
	protected PredictionDFADiscriminatorEdge(PredictionDFAState from, PredictionDFAState to, DiscriminatorDFA dfa) {
		connect(from, to);
	}
	
	@Override
	public boolean moves(IStream input) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public String getLabel() {
		return dfa.getLabel();
	}
}
