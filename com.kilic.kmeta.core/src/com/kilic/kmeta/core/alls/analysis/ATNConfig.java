package com.kilic.kmeta.core.alls.analysis;

import com.kilic.kmeta.core.alls.atn.ATNState;
import com.kilic.kmeta.core.alls.atn.IATNEdge;

public class ATNConfig {
	IATNEdge edge;
	IATNEdge alternative;
	RegularCallStack callStack;

	public ATNConfig(IATNEdge edge, IATNEdge alternative, RegularCallStack callStack) {
		this.edge = edge;
		this.alternative = alternative;
		this.callStack = callStack;
	}

	public IATNEdge getEdge() {
		return edge;
	}
	
	public ATNState getState() {
		return edge.getTo();
	}

	public IATNEdge getAlternative() {
		return alternative;
	}

	public RegularCallStack getCallStack() {
		return callStack;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();

		result.append("[ ");
		result.append(getState());
		
		if (!getState().isDecisionState() && getState().hasNext())
			result.append("->" + getState().nextEdge().getLabel());

		result.append(", ");
		result.append(alternative);
		result.append(", ");
		result.append(callStack);
		result.append(" ]");

		return result.toString();
	}
}
