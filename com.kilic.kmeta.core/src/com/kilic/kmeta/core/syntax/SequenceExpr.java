package com.kilic.kmeta.core.syntax;

import java.util.ArrayList;
import java.util.List;

import com.kilic.kmeta.core.dfa.DFA;
import com.kilic.kmeta.core.dfa.DFAState;

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
	public DFAState appendToDFA(DFA dfa, DFAState sourceState, DFAState targetState) {
		if (targetState == null)
			targetState = dfa.createState();

		if (seq.size() == 0)
			return sourceState;

		DFAState currentSourceState = sourceState;
		for (int i = 0; i < seq.size() - 1; i++) {
			currentSourceState = seq.get(i).appendToDFA(dfa, currentSourceState, null);
		}

		// appending the last element
		seq.get(seq.size() - 1).appendToDFA(dfa, currentSourceState, targetState);

		return targetState;
	}
}
