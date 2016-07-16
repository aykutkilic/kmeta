package com.kilic.kmeta.core.alls.atn;

import com.kilic.kmeta.core.alls.stream.IStream;
import com.kilic.kmeta.core.alls.tn.EpsilonEdgeBase;

public class ATNEpsilonEdge extends EpsilonEdgeBase<ATNState> implements IATNEdge{
	protected ATNEpsilonEdge(ATNState from, ATNState to) {
		super(from, to);
	}
	
	@Override
	public boolean moves(IStream input) {
		if(input.hasEnded() && this.getTo().isFinalState())
			return true;
		
		return super.moves(input);
	}
}
