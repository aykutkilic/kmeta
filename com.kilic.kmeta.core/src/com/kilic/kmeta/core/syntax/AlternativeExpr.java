package com.kilic.kmeta.core.syntax;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.kilic.kmeta.core.alls.atn.ATN;
import com.kilic.kmeta.core.alls.atn.ATNState;

public class AlternativeExpr implements ISyntaxExpr {
	private final List<ISyntaxExpr> alternatives = new ArrayList<>();

	public AlternativeExpr(final ISyntaxExpr... exprs) {
		for (ISyntaxExpr e : exprs)
			alternatives.add(e);
	}

	public void addAlternative(final ISyntaxExpr alternative) {
		alternatives.add(alternative);
	}

	public Collection<ISyntaxExpr> getAlternatives() {
		return alternatives;
	}

	@Override
	public ATNState appendToATN(final ATN atn, final ATNState sourceState, ATNState targetState) {
		if(targetState == null)
			targetState = atn.createState();
		
		final ATNState decisionState = atn.createState();
		atn.createEpsilonEdge(sourceState, decisionState);
		
		for(ISyntaxExpr alternative : alternatives) {
			ATNState altStartState = atn.createState();
			atn.createEpsilonEdge(decisionState, altStartState);
			ATNState altFinalState = alternative.appendToATN(atn, altStartState, null);
			atn.createEpsilonEdge(altFinalState, targetState);
		}
		
		return targetState;
	}
}
