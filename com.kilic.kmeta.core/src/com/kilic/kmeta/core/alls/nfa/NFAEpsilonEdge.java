package com.kilic.kmeta.core.alls.nfa;

import com.kilic.kmeta.core.alls.tn.EpsilonEdgeBase;

public class NFAEpsilonEdge extends EpsilonEdgeBase<NFAState> implements INFAEdge {
	NFAEpsilonEdge(NFAState from, NFAState to) {
		super(from, to);
	}
}
