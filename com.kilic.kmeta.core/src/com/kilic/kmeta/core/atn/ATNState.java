package com.kilic.kmeta.core.atn;

import java.util.Set;

import com.kilic.kmeta.core.dfa.DFA;

public class ATNState {
	static int stateIndexCounter = 0;

	int stateIndex;
	Set<IATNEdge> in;
	Set<IATNEdge> out;
	boolean isFinal;
	ATN atn;
	DFA predictionDFA;

	ATNState(ATN atn) {
		this.atn = atn;
		this.stateIndex = stateIndexCounter++;
	}

	public ATN getATN() {
		return atn;
	}
	
	public int getStateIndex() {
		return stateIndex;
	}

	public boolean isFinal() {
		return isFinal;
	}
	
	public void setIsFinal(boolean isFinal) {
		this.isFinal = isFinal;
	}
	
	public void addIn(IATNEdge edge) {
		in.add(edge);
	}
	
	public void addOut(IATNEdge edge) {
		out.add(edge);
	}
	
	public boolean isDecisionState() {
		return out.size()>1;
	}
	
	public boolean hasNext() {
		return out.size() == 1;
	}
	
	public IATNEdge	nextEdge() {
		assert(out.size() == 1);
		return out.iterator().next();
	}
	
	public ATNState nextState() {
		assert(out.size() == 1);
		return nextEdge().getTo();
	}
	
	@Override
	public boolean equals(Object other) {
		if( this == other ) return true;
		
		if (!(other instanceof ATNState))
			return false;

		return stateIndex == ((ATNState) other).getStateIndex();
	}

	@Override
	public int hashCode() {
		return stateIndex;
	}

	
	@Override
	public String toString() {
		if(isDecisionState())
			return "[D " + stateIndex + "]";
		else if(isFinal)
			return "[[" + stateIndex + "]]";
		
		return "[" + stateIndex + "]";
	}
}
