package com.kilic.kmeta.core.alls.predictiondfa;

import com.kilic.kmeta.core.alls.tn.CharSetEdgeBase;
import com.kilic.kmeta.core.util.CharSet;

public class PredictionDFACharSetEdge extends CharSetEdgeBase<PredictionDFAState> implements IPredictionDFAEdge {
	protected PredictionDFACharSetEdge(CharSet charSet) {
		super(charSet);
	}
}
