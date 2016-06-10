package com.kilic.kmeta.core.syntax;

import java.util.ArrayList;
import java.util.List;

import com.kilic.kmeta.core.dfa.Automaton;
import com.kilic.kmeta.core.dfa.AutomatonState;

public class AlternativeExpr implements ISyntaxExpr {
	List<ISyntaxExpr> alternatives = new ArrayList<>();

	public void addAlternative(ISyntaxExpr alternative) {
		alternatives.add(alternative);
	}

	public ISyntaxExpr[] getAlternatives() {
		return (ISyntaxExpr[]) alternatives.toArray();
	}

	@Override
	public AutomatonState appendToDFA(Automaton dfa, AutomatonState sourceState, AutomatonState targetState) {
		if (targetState == null)
			targetState = dfa.createState();

		for (ISyntaxExpr alt : alternatives) {
			alt.appendToDFA(dfa, sourceState, targetState);
		}

		return targetState;
	}
}
