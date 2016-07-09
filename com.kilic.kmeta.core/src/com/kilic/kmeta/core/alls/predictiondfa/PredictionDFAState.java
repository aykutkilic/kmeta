package com.kilic.kmeta.core.alls.predictiondfa;

import java.util.Set;

import com.kilic.kmeta.core.alls.analysis.ATNConfigSet;
import com.kilic.kmeta.core.alls.atn.IATNEdge;
import com.kilic.kmeta.core.alls.stream.IStream;
import com.kilic.kmeta.core.alls.tn.IEdge;

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
