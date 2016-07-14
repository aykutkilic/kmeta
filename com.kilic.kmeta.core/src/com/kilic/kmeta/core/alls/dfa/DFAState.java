package com.kilic.kmeta.core.alls.dfa;

import com.kilic.kmeta.core.alls.tn.IntegerKeyedState;

public class DFAState extends IntegerKeyedState<IDFAEdge, DFAState> {
	protected DFAState(DFA container) {
		super(container);
	}
}
