package com.kilic.kmeta.core.syntax;

import java.util.ArrayList;
import java.util.List;

import com.kilic.kmeta.core.atn.ATN;
import com.kilic.kmeta.core.atn.DecisionState;
import com.kilic.kmeta.core.atn.IATNState;
import com.kilic.kmeta.core.atn.RegularATNState;
import com.kilic.kmeta.core.dfa.DFA;
import com.kilic.kmeta.core.dfa.DFAState;

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
	public IATNState appendToATN(ATN atn, IATNState sourceState, IATNState targetState) {
		if(targetState == null)
			targetState = atn.createRegularState();
		
		DecisionState decisionState = atn.createDecisionState();
		atn.createEpsilonEdge(sourceState, decisionState);
		
		for(ISyntaxExpr alternative : alternatives) {
			RegularATNState altStartState = atn.createRegularState();
			atn.createEpsilonEdge(decisionState, altStartState);
			IATNState altFinalState = alternative.appendToATN(atn, altStartState, null);
			atn.createEpsilonEdge(altFinalState, targetState);
		}
		
		return targetState;
	}
}
