package com.kilic.kmeta.core.syntax;

import com.kilic.kmeta.core.alls.atn.ATN;
import com.kilic.kmeta.core.alls.atn.ATNState;
import com.kilic.kmeta.core.alls.parser.IMutator;

public class MutatorExpr implements ISyntaxExpr {
	final IMutator mutator;

	public MutatorExpr(final IMutator mutator) {
		this.mutator = mutator;
	}

	@Override
	public ATNState appendToATN(final ATN atn, final ATNState sourceState, ATNState targetState) {
		if (targetState == null)
			targetState = atn.createState();
		atn.createMutatorEdge(sourceState, targetState, mutator);
		return targetState;
	}
}
