package com.kilic.kmeta.core.syntax;

import com.kilic.kmeta.core.alls.atn.ATN;
import com.kilic.kmeta.core.alls.atn.ATNState;
import com.kilic.kmeta.core.meta.MAttribute;
import com.kilic.kmeta.core.meta.MFeature;
import com.kilic.kmeta.core.meta.MReference;

public class FeatureRef implements ISyntaxExpr {
	MFeature feature;

	public FeatureRef(MFeature feature) {
		this.feature = feature;
	}

	public MFeature getFeature() {
		return feature;
	}

	public void setAttribute(MFeature feature) {
		this.feature = feature;
	}

	@Override
	public String toString() {
		if (feature == null)
			return "()";
		if (feature instanceof MReference) {
			return "(" + ((MReference) feature).getTargetClass().getName() + ")";
		} else if (feature instanceof MAttribute) {
			return "(" + ((MAttribute) feature).getType().getName() + ")";
		}

		return "(??)";
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof FeatureRef)
			return this.feature == ((FeatureRef) other).feature;

		return false;
	}

	@Override
	public ATNState appendToATN(ATN atn, ATNState sourceState, ATNState targetState) {
		// TODO Auto-generated method stub
		return null;
	}
}
