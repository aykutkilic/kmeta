package com.kilic.kmeta.core.automaton;

public abstract class AutomatonTransitionBase implements IAutomatonTransition {
	private AutomatonState fromState, toState;
	private Object attachedObject;

	public AutomatonTransitionBase(AutomatonState fromState, AutomatonState toState) {
		this.fromState = fromState;
		this.toState = toState;
	}

	public AutomatonState getFromState() {
		return fromState;
	}

	public AutomatonState getToState() {
		return toState;
	}

	@Override
	public void attachObject(Object o) {
		attachedObject = o;
	}

	@Override
	public Object getAttachedObject() {
		return attachedObject;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append(fromState.toString());
		
		String label = getLabel();
		if(label.isEmpty())
			str.append("->");
		else {
			str.append('-');
			str.append(label);
			str.append("->");
		}
		
		str.append(toState.toString());
		return str.toString();
	}
}
