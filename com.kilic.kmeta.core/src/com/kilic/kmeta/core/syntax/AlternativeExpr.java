package com.kilic.kmeta.core.syntax;

import java.util.ArrayList;
import java.util.List;

import com.kilic.kmeta.core.automaton.Automaton;
import com.kilic.kmeta.core.automaton.AutomatonState;

public class AlternativeExpr implements ISyntaxExpr {
	List<ISyntaxExpr> alternatives = new ArrayList<>();

	public AlternativeExpr(ISyntaxExpr... exprs) {
		for (ISyntaxExpr e : exprs)
			alternatives.add(e);
	}

	public void addAlternative(ISyntaxExpr alternative) {
		alternatives.add(alternative);
	}

	public ISyntaxExpr[] getAlternatives() {
		return (ISyntaxExpr[]) alternatives.toArray();
	}

	@Override
	public AutomatonState appendToNFA(Automaton nfa, AutomatonState sourceState, AutomatonState targetState) {
		if (targetState == null)
			targetState = nfa.createState();

		for (ISyntaxExpr alt : alternatives) {
			alt.appendToNFA(nfa, sourceState, targetState);
		}

		return targetState;
	}
}
