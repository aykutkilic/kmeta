package com.kilic.kmeta.core.tests;

import com.kilic.kmeta.core.dfa.DFA;
import com.kilic.kmeta.core.dfa.DFAState;
import com.kilic.kmeta.core.syntax.ISyntaxExpr;

// for testing purposes.
public class AutomatonCallSynExpr implements ISyntaxExpr {
	DFA callee;
	
	AutomatonCallSynExpr(DFA callee) {
		this.callee = callee;
	}
	
	@Override
	public DFAState appendToNFA(DFA nfa, DFAState sourceState, DFAState targetState) {
		if (targetState == null)
			targetState = nfa.createState();
		
		nfa.createCallTransition(sourceState, targetState, callee);

		return targetState;
	}
}
