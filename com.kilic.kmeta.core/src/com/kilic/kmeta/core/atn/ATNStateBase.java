package com.kilic.kmeta.core.atn;

import java.util.HashSet;
import java.util.Set;

import com.kilic.kmeta.core.dfa.DFA;

public abstract class ATNStateBase implements IATNState {
	static int stateIndexCounter = 0;

	int stateIndex;
	
	ATNStateBase() {
		this.stateIndex = stateIndexCounter++;
	}
	
	public int getStateIndex() {
		return stateIndex;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof IATNState))
			return false;

		return stateIndex == ((IATNState) other).getStateIndex();
	}

	@Override
	public int hashCode() {
		return stateIndex;
	}
}
