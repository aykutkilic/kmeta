package com.kilic.kmeta.core.alls.analysis;

import com.kilic.kmeta.core.alls.atn.ATNState;
import com.kilic.kmeta.core.alls.atn.IATNEdge;

public class ATNConfig {
	ATNState state;
	IATNEdge alternative;
	RegularCallStack callStack;

	public ATNConfig(ATNState state, IATNEdge alternative, RegularCallStack callStack) {
		this.state = state;
		this.alternative = alternative;
		this.callStack = callStack;
	}

	public ATNState getState() {
		return state;
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
