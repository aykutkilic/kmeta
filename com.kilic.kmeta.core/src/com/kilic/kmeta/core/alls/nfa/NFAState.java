package com.kilic.kmeta.core.alls.nfa;

import com.kilic.kmeta.core.alls.tn.ITransitionNetwork;
import com.kilic.kmeta.core.alls.tn.IntegerKeyedState;

public class NFAState extends IntegerKeyedState<INFAEdge, NFAState> {
	protected NFAState(ITransitionNetwork<Integer, NFAState> container) {
		super(container);
	}
}
