package com.kilic.kmeta.core.syntax;

import java.util.ArrayList;
import java.util.List;

import com.kilic.kmeta.core.automaton.Automaton;
import com.kilic.kmeta.core.automaton.AutomatonState;

public class SequenceExpr implements ISyntaxExpr {
	List<ISyntaxExpr> seq = new ArrayList<>();

	public SequenceExpr(ISyntaxExpr... exprs) {
		for (ISyntaxExpr e : exprs)
			seq.add(e);
	}

	public void appendSeq(ISyntaxExpr elem) {
		seq.add(elem);
	}

	public ISyntaxExpr[] getSequence() {
		return (ISyntaxExpr[]) seq.toArray();
	}

	@Override
	public AutomatonState appendToNFA(Automaton nfa, AutomatonState sourceState, AutomatonState targetState) {
		if (targetState == null)
			targetState = nfa.createState();

		if (seq.size() == 0)
			return sourceState;

		AutomatonState currentSourceState = sourceState;
		for (int i = 0; i < seq.size() - 1; i++) {
			currentSourceState = seq.get(i).appendToNFA(nfa, currentSourceState, null);
		}

		// appending the last element
		seq.get(seq.size() - 1).appendToNFA(nfa, currentSourceState, targetState);

		return targetState;
	}
}
