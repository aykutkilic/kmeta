package com.kilic.kmeta.core.alls.predictiondfa;

import java.util.Set;

import com.kilic.kmeta.core.alls.atn.IATNEdge;
import com.kilic.kmeta.core.alls.stream.IStream;
import com.kilic.kmeta.core.alls.tn.EdgeBase;

public class PredictionDFAEdge extends EdgeBase<PredictionDFAState> implements IPredictionDFAEdge {
	Set<IATNEdge> matchingATNEdges;
	String label;

	PredictionDFAEdge(PredictionDFAState from, PredictionDFAState to, Set<IATNEdge> matchingEdges) {
		this.matchingATNEdges = matchingEdges;
		connect(from, to);
		this.label = computeLabel();
	}
	
	String computeLabel() {
		StringBuilder result = new StringBuilder();
		
		for(IATNEdge edge : matchingATNEdges) {
			result.append(edge.getLabel());
			result.append(' ');
		}
		
		return result.toString();
	}
	
	@Override
	public boolean moves(IStream input) {
		for (IATNEdge edge : matchingATNEdges)
			if (!edge.moves(input))
				return false;

		return true;
	}

	@Override
	public String getLabel() {
		return label;
	}
}
