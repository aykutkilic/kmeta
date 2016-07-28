package com.kilic.kmeta.core.alls.predictiondfa;

import com.kilic.kmeta.core.alls.analysis.ATNConfigSet;
import com.kilic.kmeta.core.alls.atn.IATNEdge;
import com.kilic.kmeta.core.alls.stream.IStream;
import com.kilic.kmeta.core.alls.tn.StateBase;

public class PredictionDFAState extends StateBase<ATNConfigSet, IPredictionDFAEdge, PredictionDFAState> {
	public static int count = 0;
	IATNEdge decisionEdge;
	int index;
	boolean isStackSensitive;

	protected PredictionDFAState(PredictionDFA container, ATNConfigSet configSet) {
		super(container, configSet);
		type = StateType.REGULAR;
		index = count++;
		isStackSensitive = false;
	}

	public PredictionDFAState move(IStream input) {
		for (IPredictionDFAEdge edge : out) {
			if (edge.match(input) != null)
				return edge.getTo();
		}

		return null;
	}

	public IATNEdge getDecisionEdge() {
		return decisionEdge;
	}

	public boolean isStackSensitive() {
		return isStackSensitive;
	}

	public void setStackSensitivity(boolean isStackSensitive) {
		this.isStackSensitive = isStackSensitive;
	}

	public void setFinal(IATNEdge decisionEdge) {
		this.setType(StateType.FINAL);
		this.decisionEdge = decisionEdge;
	}

	@Override
	public String getLabel() {
		return String.valueOf(index);
	}

	@Override
	public String toString() {
		switch (type) {
		case FINAL:
			return "[[" + getLabel() + "]]";

		case ERROR:
			return "[E " + getLabel() + " ]";

		case REGULAR:
		default:
			return "[" + getLabel() + "]";
		}
	}
}
