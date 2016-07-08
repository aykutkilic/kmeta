package com.kilic.kmeta.core.dfa;

import java.util.Set;

import com.kilic.kmeta.core.atn.ATNConfigSet;
import com.kilic.kmeta.core.atn.IATNEdge;
import com.kilic.kmeta.core.stream.IStream;

public class PredictionDFAState extends DFAStateBase<ATNConfigSet> {
	Set<IDFAEdge<ATNConfigSet>> in, out;
	IATNEdge decisionEdge;

	protected PredictionDFAState(PredictionDFA container, ATNConfigSet configSet) {
		super(container, configSet);
		stateType = StateType.REGULAR;
	}
	
	public PredictionDFAState move(IStream input) {
		for (IDFAEdge<ATNConfigSet> edge : out) {
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
