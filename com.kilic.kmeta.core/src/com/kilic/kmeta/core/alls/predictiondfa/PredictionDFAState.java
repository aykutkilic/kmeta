package com.kilic.kmeta.core.alls.predictiondfa;

import com.kilic.kmeta.core.alls.analysis.ATNConfigSet;
import com.kilic.kmeta.core.alls.atn.IATNEdge;
import com.kilic.kmeta.core.alls.stream.IStream;
import com.kilic.kmeta.core.alls.tn.StateBase;

public class PredictionDFAState extends StateBase<ATNConfigSet, IPredictionDFAEdge, PredictionDFAState> {
	IATNEdge decisionEdge;

	protected PredictionDFAState(PredictionDFA container, ATNConfigSet configSet) {
		super(container, configSet);
		type = StateType.REGULAR;
	}
	
	public PredictionDFAState move(IStream input) {
		for (IPredictionDFAEdge edge : out) {
			if (edge.moves(input))
				return edge.getTo();
		}

		return null;
	}
	
	public IATNEdge getDecisionEdge() {
		return decisionEdge;
	}
}
