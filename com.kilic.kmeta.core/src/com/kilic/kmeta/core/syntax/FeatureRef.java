package com.kilic.kmeta.core.syntax;

import com.kilic.kmeta.core.automaton.Automaton;
import com.kilic.kmeta.core.automaton.AutomatonState;
import com.kilic.kmeta.core.meta.MFeature;

public class FeatureRef implements ISyntaxExpr {
	MFeature feature;

	public FeatureRef(MFeature feature) {
		this.feature = feature;
	}

	public MFeature getFeature() {
		return feature;
	}

	public void setFeature(MFeature feature) {
		this.feature = feature;
	}

	@Override
	public AutomatonState appendToNFA(Automaton nfa, AutomatonState sourceState, AutomatonState targetState) {
		if(targetState ==null)
			targetState = nfa.createState();
		
		nfa.createTransition( 
			sourceState, 
			targetState, 
			new Sub
		);
		
		return targetState;
	}
}
