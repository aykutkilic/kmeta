package com.kilic.kmeta.core.atn;

import java.util.HashSet;
import java.util.Set;

import com.kilic.kmeta.core.dfa.DFA;

public class ATNState {
	static int stateIndexCounter = 0;

	enum AtnStateType {
		START,
		REGULAR,
		DECISION,
		FINAL
	}
	
	int stateIndex;
	AtnStateType stateType;
	Set<IATNEdge> in, out;
	DFA predictionDFA;
	
	ATNState() {
		this(AtnStateType.REGULAR);
	}
	
	ATNState(AtnStateType stateType) {
		this.stateIndex = stateIndexCounter++;
		this.stateType = stateType;
		
		in = new HashSet<>();
		out = new HashSet<>();
	}
	
	public int getStateIndex() {
		return stateIndex;
	}
	
	public AtnStateType getType() {
		return stateType;
	}
	
	void setPredictionDFA(DFA dfa) {
		assert(stateType == AtnStateType.DECISION);
		predictionDFA = dfa; 
	}
	
	DFA getPredictionDFA() {
		assert(stateType == AtnStateType.DECISION);
		return predictionDFA; 
	}

	@Override
	public String toString() {
		switch(stateType){
		case START:
			return "[S " + stateIndex + "]";
		
		case DECISION:
			return "[D " + stateIndex + "]";
			
		case FINAL:
			return "[[" + stateIndex + "]]";

		default:
			return "[" + stateIndex + "]";
		}
		
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof ATNState))
			return false;

		return stateIndex == ((ATNState) other).getStateIndex();
	}

	@Override
	public int hashCode() {
		return stateIndex;
	}
}
