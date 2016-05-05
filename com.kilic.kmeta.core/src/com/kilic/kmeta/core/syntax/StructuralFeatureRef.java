package com.kilic.kmeta.core.syntax;

import com.kilic.kmeta.core.meta.MStructuralFeature;

public class StructuralFeatureRef implements ISyntaxExpr {
	MStructuralFeature feature;
	
	public StructuralFeatureRef(MStructuralFeature feature) {
		this.feature = feature;
	}
	
	public MStructuralFeature getFeature() { return feature; }
	public void setFeature(MStructuralFeature feature) { this.feature = feature; }
}
