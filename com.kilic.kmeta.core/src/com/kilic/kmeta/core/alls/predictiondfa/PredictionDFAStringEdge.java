package com.kilic.kmeta.core.alls.predictiondfa;

import com.kilic.kmeta.core.alls.tn.StringEdgeBase;

public class PredictionDFAStringEdge extends StringEdgeBase<PredictionDFAState> {
	protected PredictionDFAStringEdge(String string) {
		super(string);
	}
}
