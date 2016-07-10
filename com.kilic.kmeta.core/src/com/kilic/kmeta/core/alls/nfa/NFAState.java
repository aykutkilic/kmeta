package com.kilic.kmeta.core.alls.nfa;

import com.kilic.kmeta.core.alls.tn.IntegerKeyedState;
import com.kilic.kmeta.core.util.CharSet;

public class NFAState extends IntegerKeyedState<INFAEdge, NFAState> {
	protected NFAState(NFA container) {
		super(container);
	}
	
	NFAState moveByCharSet(CharSet charSet) {
		for (INFAEdge edge : getOut()) {
			if(edge instanceof NFACharSetEdge) {
				if(((NFACharSetEdge) edge).getCharSet().intersects(charSet))
					return edge.getTo();
			}
		}

		return null;
	}
}
