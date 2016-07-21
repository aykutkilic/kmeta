package com.kilic.kmeta.core.alls.predictiondfa;

import com.kilic.kmeta.core.alls.atn.IATNEdge;
import com.kilic.kmeta.core.alls.stream.IStream;
import com.kilic.kmeta.core.alls.tn.EdgeBase;

public class PredictionDFAEdge extends EdgeBase<PredictionDFAState> implements IPredictionDFAEdge {
	IATNEdge matchingATNEdge;
	String label;

	PredictionDFAEdge(PredictionDFAState from, PredictionDFAState to, IATNEdge matchingEdge) {
		this.matchingATNEdge = matchingEdge;
		connect(from, to);
		this.label = computeLabel();
	}

	String computeLabel() {
		return matchingATNEdge.getLabel();
	}

	@Override
	public String match(IStream input) {
		return matchingATNEdge.match(input);
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public String toString() {
		return getFrom().getLabel() + " -" + label + "-> " + getTo().getLabel();
	}
}
