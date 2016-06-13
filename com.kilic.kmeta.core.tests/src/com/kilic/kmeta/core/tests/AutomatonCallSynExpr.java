package com.kilic.kmeta.core.tests;

import com.kilic.kmeta.core.automaton.Automaton;
import com.kilic.kmeta.core.automaton.AutomatonState;
import com.kilic.kmeta.core.syntax.ISyntaxExpr;

// for testing purposes.
public class AutomatonCallSynExpr implements ISyntaxExpr {
	Automaton callee;
	
	AutomatonCallSynExpr(Automaton callee) {
		this.callee = callee;
	}
	
	@Override
	public AutomatonState appendToNFA(Automaton nfa, AutomatonState sourceState, AutomatonState targetState) {
		if (targetState == null)
			targetState = nfa.createState();
		
		nfa.createCallTransition(sourceState, targetState, callee);

		return targetState;
	}
}
