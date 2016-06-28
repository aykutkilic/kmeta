package com.kilic.kmeta.core.syntax;

import com.kilic.kmeta.core.dfa.Automaton;
import com.kilic.kmeta.core.dfa.AutomatonState;
import com.kilic.kmeta.core.dfa.CharSetMatcher;
import com.kilic.kmeta.core.util.CharSet;

public class CharSetExpr implements ISyntaxExpr {
	CharSet charSet;

	public CharSetExpr(CharSet charSet) {
		this.charSet = charSet;
	}

	public CharSet getCharSet() {
		return charSet;
	}

	@Override
	public AutomatonState appendToNFA(Automaton nfa, AutomatonState sourceState, AutomatonState targetState) {
		if (targetState == null)
			targetState = nfa.createState();

		nfa.createMatcherTransition(sourceState, targetState, new CharSetMatcher(charSet));

		return targetState;
	}
}
