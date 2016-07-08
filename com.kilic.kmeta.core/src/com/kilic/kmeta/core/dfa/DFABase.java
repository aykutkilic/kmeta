package com.kilic.kmeta.core.dfa;

import java.util.HashMap;
import java.util.Map;

public abstract class DFABase<SK> implements IDFA {
	String label;
	
	IDFAState startState;
	IDFAState errorState;
	
	Map<SK, IDFAState> states;
	
	protected DFABase() {
		states = new HashMap<>();
	}
	
	public void setLabel(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
	
	public IDFAState getStartState() {
		return startState;
	}

	public void setStartState(IDFAState newStartState) {
		startState = newStartState;
	}
	
	public IDFAState getErrorState() {
		return errorState;
	}
	
	public IDFAState getState(SK key) {
		return states.get(key);
	}
}
