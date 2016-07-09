package com.kilic.kmeta.core.atn;

import java.util.HashSet;
import java.util.Set;

import com.kilic.kmeta.core.predictiondfa.PredictionDFA;
import com.kilic.kmeta.core.stream.IStream;
import com.kilic.kmeta.core.tn.IEdge;
import com.kilic.kmeta.core.tn.IState;
import com.kilic.kmeta.core.tn.IntegerKeyedState;

public class ATNState extends IntegerKeyedState {
	PredictionDFA predictionDFA;

	ATNState(ATN atn) {
		super(atn);
	}

	public PredictionDFA getPredictionDFA() {
		return predictionDFA;
	}
	
	public void setPredictionDFA(PredictionDFA predictionDFA) {
		this.predictionDFA = predictionDFA;
	}
	
	public boolean isDecisionState() {
		return out.size()>1;
	}
	
	public boolean hasNext() {
		return out.size() == 1;
	}
	
	public IEdge<Integer> nextEdge() {
		assert(out.size() == 1);
		return out.iterator().next();
	}
	
	public IState<Integer> nextState() {
		assert(out.size() == 1);
		return nextEdge().getTo();
	}
	
	public Set<ATNState> move(IStream input) {
		Set<ATNState> result = new HashSet<>();
		
		for(IEdge<Integer> edge : out) {
			if(edge.moves(input))
				result.add((ATNState) edge.getTo());
		}
		
		return result;
	}
	
	@Override
	public String toString() {
		if(isDecisionState())
			return "[D " + key + "]";
		else if(isFinal())
			return "[[" + key + "]]";
		
		return "[" + key + "]";
	}
}
