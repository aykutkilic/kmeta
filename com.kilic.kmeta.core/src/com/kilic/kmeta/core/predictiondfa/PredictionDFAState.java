package com.kilic.kmeta.core.predictiondfa;

import java.util.Set;

import com.kilic.kmeta.core.analysis.ATNConfigSet;
import com.kilic.kmeta.core.atn.IATNEdge;
import com.kilic.kmeta.core.stream.IStream;
import com.kilic.kmeta.core.tn.IEdge;

public class PredictionDFAState extends DFAStateBase<ATNConfigSet> {
	Set<IEdge<ATNConfigSet>> in, out;
	IATNEdge decisionEdge;

	protected PredictionDFAState(PredictionDFA container, ATNConfigSet configSet) {
		super(container, configSet);
		stateType = StateType.REGULAR;
	}
	
	public PredictionDFAState move(IStream input) {
		for (IEdge<ATNConfigSet> edge : out) {
			if (edge.move(input))
				return (PredictionDFAState) edge.getTo();
		}

		return null;
	}
	
	public void setFinal(IATNEdge decisionEdge) {
		stateType = decisionEdge != null ? StateType.FINAL : stateType;
		this.decisionEdge = decisionEdge;
	}

	public IATNEdge getDecisionEdge() {
		return decisionEdge;
	}
}
