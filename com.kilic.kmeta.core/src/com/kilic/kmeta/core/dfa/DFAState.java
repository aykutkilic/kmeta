package com.kilic.kmeta.core.dfa;

import java.util.HashSet;
import java.util.Set;

public class DFAState {
	private static int stateIndexCounter = 0;

	Set<DFATransition> in, out;
	boolean isFinalState;
	int stateIndex;

	public DFAState() {
		stateIndex = stateIndexCounter++;

		in = new HashSet<>();
		out = new HashSet<>();
	}

	public int getStateIndex() {
		return stateIndex;
	}

	public void setFinal(boolean isFinalState) {
		this.isFinalState = isFinalState;
	}

	public boolean isFinalState() {
		return isFinalState;
	}

	public void addIncomingTransition(DFATransition trans) {
		in.add(trans);
	}

	public void addOutgoingTransition(DFATransition trans) {
		out.add(trans);
	}

	public DFATransition[] getIncomingTransitions() {
		return (DFATransition[]) in.toArray();
	}

	public DFATransition[] getOutgoingTransitions() {
		return (DFATransition[]) out.toArray();
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append('[');
		if (isFinalState)
			str.append('[');
		str.append(stateIndex);
		str.append(']');
		if (isFinalState)
			str.append(']');
		return str.toString();
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof DFAState) {
			return stateIndex == ((DFAState) other).getStateIndex();
		}

		return false;
	}

	@Override
	public int hashCode() {
		return stateIndex;
	}
}
