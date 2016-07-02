package com.kilic.kmeta.core.dfa;	

import java.util.HashSet;
import java.util.Set;

import com.kilic.kmeta.core.atn.ATNConfig;
import com.kilic.kmeta.core.atn.ATNConfigSet;

public class DFAState {
	ATNConfigSet configSet;
	Set<IDFAEdge> in, out;
	
	boolean isFinalState;
	DFA container;

	protected DFAState(DFA container) {
		in = new HashSet<>();
		out = new HashSet<>();
		
		this.container = container;
	}
	
	public DFA getContainer() {
		return container;
	}

	public void setFinal(boolean isFinalState) {
		this.isFinalState = isFinalState;
	}

	public boolean isFinalState() {
		return isFinalState;
	}

	public void addIn(IDFAEdge edge) {
		in.add(edge);
	}

	public void addOut(IDFAEdge edge) {
		out.add(edge);
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
		if(this == other) 
			return true;
		
		if(!(other instanceof DFAState))
			return false;

		return configSet.equals(((DFAState)other).configSet);
	}

	@Override
	public int hashCode() {
		return configSet.hashCode();
	}
}
