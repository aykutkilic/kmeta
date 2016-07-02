package com.kilic.kmeta.core.syntax;

import java.util.ArrayList;
import java.util.List;

import com.kilic.kmeta.core.atn.ATN;
import com.kilic.kmeta.core.atn.ATNState;

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
	public ATNState appendToATN(ATN atn, ATNState sourceState, ATNState targetState) {
		if (targetState == null)
			targetState = atn.createState();

		if (seq.size() == 0)
			return sourceState;

		ATNState currentSourceState = sourceState;
		for (int i = 0; i < seq.size() - 1; i++) {
			currentSourceState = seq.get(i).appendToATN(atn, currentSourceState, null);
		}

		// appending the last element
		seq.get(seq.size() - 1).appendToATN(atn, currentSourceState, targetState);

		return targetState;

	}
}
