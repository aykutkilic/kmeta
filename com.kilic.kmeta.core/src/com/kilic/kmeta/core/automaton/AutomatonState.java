package com.kilic.kmeta.core.automaton;

import java.util.HashSet;
import java.util.Set;

public class AutomatonState {
	private static int stateIndexCounter = 0;

	Set<AutomatonTransition> in, out;
	boolean isFinalState;
	int stateIndex;
	Object attachedObject;

	public AutomatonState() {
		stateIndex = stateIndexCounter++;

		in = new HashSet<>();
		out = new HashSet<>();
	}

	public void attachObject(Object o) {
		attachedObject = o;
	}

	public Object getAttachedObject() {
		return attachedObject;
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

	public void addIncomingTransition(AutomatonTransition trans) {
		in.add(trans);
	}

	public void addOutgoingTransition(AutomatonTransition trans) {
		out.add(trans);
	}

	public Set<AutomatonTransition> getIncomingTransitions() {
		return in;
	}

	public Set<AutomatonTransition> getOutgoingTransitions() {
		return out;
	}

	public AutomatonState move(IMatcher m) {
		for (AutomatonTransition t : out) {
			if (t.getGuardCondition() == null)
				continue;

			if (t.getGuardCondition().equals(m))
				return t.getToState();
		}

		return null;
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
		if (other instanceof AutomatonState) {
			return stateIndex == ((AutomatonState) other).getStateIndex();
		}

		return false;
	}

	@Override
	public int hashCode() {
		return stateIndex;
	}
}
