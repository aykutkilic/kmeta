package com.kilic.kmeta.core.syntax;

import com.kilic.kmeta.core.dfa.DFA;
import com.kilic.kmeta.core.dfa.DFAState;

public class DFAFactory {
	public static DFA createMatcherDFAFromSyntaxExpr(ISyntaxExpr e) {
		DFA resultDFA = new DFA();
		DFAState startState = resultDFA.createState();
		resultDFA.setStartState(startState);

		e.appendToDFA(resultDFA, startState, null);

		return resultDFA;
	}
}
