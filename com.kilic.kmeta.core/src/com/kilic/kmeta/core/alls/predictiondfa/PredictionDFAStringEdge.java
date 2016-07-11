package com.kilic.kmeta.core.alls.predictiondfa;

import com.kilic.kmeta.core.alls.tn.StringEdgeBase;

public class PredictionDFAStringEdge extends StringEdgeBase<PredictionDFAState> implements IPredictionDFAEdge {
	protected PredictionDFAStringEdge(PredictionDFAState from, PredictionDFAState to, String string) {
		super(from, to, string);
	}
}
