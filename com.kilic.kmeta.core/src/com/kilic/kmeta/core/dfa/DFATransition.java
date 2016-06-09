package com.kilic.kmeta.core.dfa;

public class DFATransition {
	private DFAState fromState, toState;
	private IMatcher guardCondition;

	public DFATransition(DFAState fromState, DFAState toState, IMatcher condition) {
		this.fromState = fromState;
		this.toState = toState;
		this.guardCondition = condition;
	}

	public DFAState getFromState() {
		return fromState;
	}

	public DFAState getToState() {
		return toState;
	}

	public IMatcher getGuardCondition() {
		return guardCondition;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append(fromState.toString());
		str.append('-');
		if (guardCondition == null)
			str.append((char) 0xDE);
		else
			str.append(guardCondition.toString());
		str.append("->");
		str.append(toState.toString());
		return str.toString();
	}
}
