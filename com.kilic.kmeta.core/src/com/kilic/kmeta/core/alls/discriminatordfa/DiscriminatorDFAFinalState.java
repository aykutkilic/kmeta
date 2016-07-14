package com.kilic.kmeta.core.alls.discriminatordfa;

import java.util.Set;

import com.kilic.kmeta.core.alls.dfa.DFA;
import com.kilic.kmeta.core.alls.dfa.DFAState;

public class DiscriminatorDFAFinalState extends DFAState {
	Set<DFA> matchingDFAs;
	
	protected DiscriminatorDFAFinalState(DFA container, Set<DFA> matchingDFAs) {
		super(container);
		setType(StateType.FINAL);
		this.matchingDFAs = matchingDFAs;
	}

}
