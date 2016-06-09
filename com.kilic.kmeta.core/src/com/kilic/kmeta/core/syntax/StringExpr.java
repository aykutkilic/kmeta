package com.kilic.kmeta.core.syntax;

import com.kilic.kmeta.core.dfa.DFA;
import com.kilic.kmeta.core.dfa.DFAState;
import com.kilic.kmeta.core.dfa.StringMatcher;

public class StringExpr implements ISyntaxExpr {
	String string;

	public StringExpr(String string) {
		this.string = string;
	}

	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}

	@Override
	public DFAState appendToDFA(DFA dfa, DFAState sourceState, DFAState targetState) {
		if (targetState == null)
			targetState = dfa.createState();

		dfa.createTransition(sourceState, targetState, new StringMatcher(string));

		return targetState;
	}
}
