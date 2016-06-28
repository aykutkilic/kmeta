package com.kilic.kmeta.core.dfa;

import com.kilic.kmeta.core.stream.IStream;
import com.kilic.kmeta.core.syntax.FeatureRef;

public class AttributeRefMatcher implements IMatcher {
	FeatureRef attributeRef;

	public AttributeRefMatcher(FeatureRef attributeRef) {
		this.attributeRef = attributeRef;
	}

	@Override
	public boolean match(IStream stream) {
		return false;
	}
}
