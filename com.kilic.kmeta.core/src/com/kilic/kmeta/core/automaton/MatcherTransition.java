package com.kilic.kmeta.core.automaton;

public class MatcherTransition extends AutomatonTransitionBase {
	private IMatcher matcher;

	public MatcherTransition(AutomatonState fromState, AutomatonState toState, IMatcher matcher) {
		super(fromState,toState);
		assert(matcher!=null);
		this.matcher = matcher;
	}

	public IMatcher getMatcher() {
		return matcher;
	}

	public String getLabel() {
		return matcher.toString();
	}
	
	public boolean isEquivalent(IAutomatonTransition other) {
		if(other instanceof MatcherTransition)
			return matcher.equals(((MatcherTransition) other).matcher);
			
		return false;
	}
}
