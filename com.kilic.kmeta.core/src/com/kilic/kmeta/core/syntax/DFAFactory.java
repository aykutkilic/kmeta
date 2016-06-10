package com.kilic.kmeta.core.syntax;

import com.kilic.kmeta.core.dfa.Automaton;
import com.kilic.kmeta.core.dfa.AutomatonState;

public class DFAFactory {
	public static Automaton createMatcherDFAFromSyntaxExpr(ISyntaxExpr e) {
		Automaton resultDFA = new Automaton();
		AutomatonState startState = resultDFA.createState();
		resultDFA.setStartState(startState);

		e.appendToDFA(resultDFA, startState, null);

		return resultDFA;
	}
}
