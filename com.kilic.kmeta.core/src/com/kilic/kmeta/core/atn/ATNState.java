package com.kilic.kmeta.core.atn;

import java.util.HashSet;
import java.util.Set;

public class ATNState {
	private static int stateIndexCounter = 0;

	int stateIndex;
	Set<IATNEdge> in, out;

	ATNState() {
		this.stateIndex = stateIndexCounter++;

		in = new HashSet<>();
		out = new HashSet<>();
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
