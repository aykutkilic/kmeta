package com.kilic.kmeta.core.syntax;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.kilic.kmeta.core.alls.atn.ATN;
import com.kilic.kmeta.core.alls.atn.ATNState;

public class SequenceExpr implements ISyntaxExpr {
	private List<ISyntaxExpr> seq = new ArrayList<>();

	public SequenceExpr(final ISyntaxExpr... exprs) {
		for (ISyntaxExpr e : exprs)
			seq.add(e);
	}

	public void appendSeq(final ISyntaxExpr elem) {
		seq.add(elem);
	}

	public Collection<ISyntaxExpr> getSequence() {
		return seq;
	}

	@Override
	public ATNState appendToATN(final ATN atn, final ATNState sourceState, ATNState targetState) {
		if (targetState == null)
			targetState = atn.createState();

		if (seq.size() == 0) {
			atn.createEpsilonEdge(sourceState, targetState);
			return targetState;
		}

		ATNState currentSourceState = sourceState;
		for (int i = 0; i < seq.size() - 1; i++) {
			currentSourceState = seq.get(i).appendToATN(atn, currentSourceState, null);
		}

		// appending the last element
		seq.get(seq.size() - 1).appendToATN(atn, currentSourceState, targetState);

		return targetState;

	}
}
