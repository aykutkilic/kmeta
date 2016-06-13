package com.kilic.kmeta.core.automaton;

public interface IAutomatonTransition extends IAttachable {
	AutomatonState getFromState();
	AutomatonState getToState();
	
	// returns true if the guard conditions are the same.
	boolean isEquivalent(IAutomatonTransition other);
	
	String getLabel();
}
