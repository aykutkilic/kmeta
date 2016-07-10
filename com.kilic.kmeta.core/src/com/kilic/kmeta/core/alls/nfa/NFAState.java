package com.kilic.kmeta.core.alls.nfa;

import com.kilic.kmeta.core.alls.tn.IntegerKeyedState;

public class NFAState extends IntegerKeyedState<INFAEdge, NFAState> {
	protected NFAState(NFA container) {
		super(container);
	}
	
	NFAState move(INFAEdge edge) {
		for (INFAEdge out : getOut()) {
			if (out.equals(edge))
				return out.getTo();
		}

		return null;
	}
}
