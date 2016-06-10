package com.kilic.kmeta.core.syntax;

import com.kilic.kmeta.core.dfa.CharSetMatcher;
import com.kilic.kmeta.core.dfa.Automaton;
import com.kilic.kmeta.core.dfa.AutomatonState;
import com.kilic.kmeta.core.discriminator.CharSet;

public class CharSetExpr implements ISyntaxExpr {
	CharSet charSet;

	public CharSetExpr(CharSet charSet) {
		this.charSet = charSet;
	}

	public CharSet getCharSet() {
		return charSet;
	}

	@Override
	public AutomatonState appendToDFA(Automaton dfa, AutomatonState sourceState, AutomatonState targetState) {
		if (targetState == null)
			targetState = dfa.createState();

		dfa.createTransition(sourceState, targetState, new CharSetMatcher(charSet));

		return targetState;
	}
}
