package com.kilic.kmeta.core.alls.atn;

import com.kilic.kmeta.core.alls.parser.IMutator;
import com.kilic.kmeta.core.alls.stream.IStream;

public class ATNMutatorEdge extends ATNEdgeBase {
	private final IMutator mutator;

	ATNMutatorEdge(final ATNState from, final ATNState to, final IMutator mutator) {
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
	public String match(final IStream input) {
		return "";
	}
}
