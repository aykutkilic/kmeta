package com.kilic.kmeta.core.alls.atn;

import java.util.HashSet;
import java.util.Set;

import com.kilic.kmeta.core.alls.predictiondfa.PredictionDFA;
import com.kilic.kmeta.core.alls.stream.IStream;
import com.kilic.kmeta.core.alls.tn.IntegerKeyedState;

public class ATNState extends IntegerKeyedState<IATNEdge, ATNState> {
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

	public boolean isFinalState() {
		return type == StateType.FINAL;
	}

	public boolean isDecisionState() {
		return out.size() > 1;
	}

	public boolean hasNext() {
		return out.size() == 1;
	}

	public IATNEdge nextEdge() {
		assert (out.size() == 1);
		return out.iterator().next();
	}

	public ATNState nextState() {
		assert (out.size() == 1);
		return nextEdge().getTo();
	}

	public Set<IATNEdge> move(IStream input) {
		Set<IATNEdge> result = new HashSet<>();

		for (IATNEdge edge : out) {
			if (edge.match(input)!=null)
				result.add(edge);
		}

		return result;
	}

	@Override
	public String toString() {
		if (isDecisionState())
			return "[D " + key + "]";
		else if (isFinal())
			return "[[" + key + "]]";

		return "[" + key + "]";
	}
}
