package com.kilic.kmeta.core.alls.atn;

import com.kilic.kmeta.core.alls.parser.IMutator;
import com.kilic.kmeta.core.alls.stream.IStream;

public class ATNMutatorEdge extends ATNEdgeBase {
	IMutator mutator;

	ATNMutatorEdge(ATNState from, ATNState to, IMutator mutator) {
		this.mutator = mutator;
		connect(from, to);
	}

	public IMutator getMutator() {
		return mutator;
	}
	
	@Override
	public String getLabel() {
		return mutator.getLabel();
	}

	@Override
	public String match(IStream input) {
		return null;
	}
}
