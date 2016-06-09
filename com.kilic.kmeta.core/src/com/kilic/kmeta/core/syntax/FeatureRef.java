package com.kilic.kmeta.core.syntax;

import com.kilic.kmeta.core.dfa.DFA;
import com.kilic.kmeta.core.dfa.DFAState;
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
	public DFAState appendToDFA(DFA dfa, DFAState sourceState, DFAState targetState) {
		return null;
	}
}
