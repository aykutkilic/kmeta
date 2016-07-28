package com.kilic.kmeta.core.alls.predictiondfa;

import com.kilic.kmeta.core.alls.stream.IStream;
import com.kilic.kmeta.core.alls.tn.EdgeBase;
import com.kilic.kmeta.core.util.CharSet;

public class PredictionDFAEdge extends EdgeBase<PredictionDFAState> implements IPredictionDFAEdge {
	CharSet charSet;
	String label;

	PredictionDFAEdge(PredictionDFAState from, PredictionDFAState to, CharSet charSet) {
		this.charSet = charSet;
		connect(from, to);
		this.label = computeLabel();
	}

	String computeLabel() {
		return charSet.toString();
	}

	@Override
	public String match(IStream input) {
		char c = input.lookAheadChar(0);
		return charSet.containsSingleton(c) ? String.valueOf(c) : null;
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
