package com.kilic.kmeta.core.alls.analysis;

import com.kilic.kmeta.core.alls.atn.ATNState;
import com.kilic.kmeta.core.alls.atn.IATNEdge;

public class ATNConfig {
	final ATNState state;
	final IATNEdge alternative;
	final RegularCallStack callStack;

	public ATNConfig(final ATNState state, final IATNEdge alternative, final RegularCallStack callStack) {
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
	public int hashCode() {
		return state.hashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;

		if (!(other instanceof ATNConfig))
			return false;

		ATNConfig o = (ATNConfig) other;

		if (!state.equals(o.state))
			return false;

		if (!alternative.equals(o.alternative))
			return false;

		if (!callStack.equals(o.callStack))
			return false;

		return true;
	}

	@Override
	public String toString() {
		final StringBuilder result = new StringBuilder();

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
