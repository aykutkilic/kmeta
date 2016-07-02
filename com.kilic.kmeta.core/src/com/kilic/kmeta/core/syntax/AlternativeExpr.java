package com.kilic.kmeta.core.syntax;

import java.util.ArrayList;
import java.util.List;

import com.kilic.kmeta.core.atn.ATN;
import com.kilic.kmeta.core.atn.ATNState;

public class AlternativeExpr implements ISyntaxExpr {
	List<ISyntaxExpr> alternatives = new ArrayList<>();

	public AlternativeExpr(ISyntaxExpr... exprs) {
		for (ISyntaxExpr e : exprs)
			alternatives.add(e);
	}

	public void addAlternative(ISyntaxExpr alternative) {
		alternatives.add(alternative);
	}

	public ISyntaxExpr[] getAlternatives() {
		return (ISyntaxExpr[]) alternatives.toArray();
	}

	@Override
	public ATNState appendToATN(ATN atn, ATNState sourceState, ATNState targetState) {
		if(targetState == null)
			targetState = atn.createState();
		
		ATNState decisionState = atn.createState();
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
