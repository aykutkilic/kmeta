package com.kilic.kmeta.core.atn;

import java.util.HashSet;
import java.util.Set;

import com.kilic.kmeta.core.dfa.DFA;

public class DecisionState extends ATNStateBase {
	Set<IATNEdge> in, out;
	DFA predictionDFA;
	
	DecisionState() {
		super();
		
		in = new HashSet<>();
		out = new HashSet<>();
	}
	
	@Override
	public void addIn(IATNEdge edge) {
		in.add(edge);
	}
	
	@Override
	public void addOut(IATNEdge edge) {
		out.add(edge);
	}
	
	@Override
	public String toString() {
		return "[D " + getStateIndex() + "]";
	}
}
