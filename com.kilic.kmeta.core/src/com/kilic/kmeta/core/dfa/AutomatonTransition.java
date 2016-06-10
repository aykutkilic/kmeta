package com.kilic.kmeta.core.dfa;

public class AutomatonTransition implements IAttachable {
	private AutomatonState fromState, toState;
	private IMatcher guardCondition;
	private Object attachedObject;

	public AutomatonTransition(AutomatonState fromState, AutomatonState toState, IMatcher condition) {
		this.fromState = fromState;
		this.toState = toState;
		this.guardCondition = condition;
	}

	public AutomatonState getFromState() {
		return fromState;
	}

	public AutomatonState getToState() {
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

	@Override
	public void attachObject(Object o) {
		attachedObject = o;
	}

	@Override
	public Object getAttachedObject() {
		return attachedObject;
	}
}
