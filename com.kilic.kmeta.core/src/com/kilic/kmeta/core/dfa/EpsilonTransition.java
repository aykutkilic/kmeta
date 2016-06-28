package com.kilic.kmeta.core.dfa;

public class EpsilonTransition extends AutomatonTransitionBase {
	public EpsilonTransition(AutomatonState fromState, AutomatonState toState) {
		super(fromState, toState);
	}

	public String getLabel() { 
		return "<e>";
	}
	
	public boolean isEquivalent(IAutomatonTransition other) {
		return other instanceof EpsilonTransition;
	}
}
