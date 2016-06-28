package com.kilic.kmeta.core.atn;

public class ATNState {
	private static int stateIndexCounter = 0;

	int stateIndex;

	public ATNState() {
		this.stateIndex = stateIndexCounter++;
	}

	public int getStateIndex() {
		return stateIndex;
	}

	@Override
	public String toString() {
		return "[" + stateIndex + "]";
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof ATNState))
			return false;

		return stateIndex == ((ATNState) other).getStateIndex();
	}

	@Override
	public int hashCode() {
		return stateIndex;
	}
}
