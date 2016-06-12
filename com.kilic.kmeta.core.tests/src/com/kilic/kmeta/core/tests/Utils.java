package com.kilic.kmeta.core.tests;

import com.kilic.kmeta.core.automaton.Automaton;
import com.kilic.kmeta.core.automaton.AutomatonState;
import com.kilic.kmeta.core.syntax.ISyntaxExpr;

public class Utils {
	public static Automaton createNFAFromSyntax(ISyntaxExpr e) {
		Automaton enfa = new Automaton();
		AutomatonState startState = enfa.createState();
		enfa.setStartState(startState);
		AutomatonState finalState = e.appendToNFA(enfa, startState, null);
		finalState.setFinal(true);

		return enfa;
	}
}
