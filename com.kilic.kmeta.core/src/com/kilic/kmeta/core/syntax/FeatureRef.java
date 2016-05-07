package com.kilic.kmeta.core.syntax;

import com.kilic.kmeta.core.meta.MFeature;

public class FeatureRef implements ISyntaxExpr {
	MFeature feature;
	
	public FeatureRef(MFeature feature) {
		this.feature = feature;
	}
	
	public MFeature getFeature() { return feature; }
	public void setFeature(MFeature feature) { this.feature = feature; }
}
