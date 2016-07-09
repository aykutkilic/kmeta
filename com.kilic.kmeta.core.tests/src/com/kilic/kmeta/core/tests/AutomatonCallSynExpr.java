package com.kilic.kmeta.core.tests;

import com.kilic.kmeta.core.alls.predictiondfa.PredictionDFA;
import com.kilic.kmeta.core.alls.predictiondfa.PredictionDFAState;
import com.kilic.kmeta.core.alls.syntax.ISyntaxExpr;

// for testing purposes.
public class AutomatonCallSynExpr implements ISyntaxExpr {
	PredictionDFA callee;
	
	AutomatonCallSynExpr(PredictionDFA callee) {
		this.callee = callee;
	}
	
	@Override
	public PredictionDFAState appendToNFA(PredictionDFA nfa, PredictionDFAState sourceState, PredictionDFAState targetState) {
		if (targetState == null)
			targetState = nfa.createState();
		
		nfa.createCallTransition(sourceState, targetState, callee);

		return targetState;
	}
}
