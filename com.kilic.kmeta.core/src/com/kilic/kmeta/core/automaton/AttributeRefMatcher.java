package com.kilic.kmeta.core.automaton;

import com.kilic.kmeta.core.stream.IStream;
import com.kilic.kmeta.core.syntax.AttributeRef;

public class AttributeRefMatcher implements IMatcher {
	AttributeRef attributeRef;

	public AttributeRefMatcher(AttributeRef attributeRef) {
		this.attributeRef = attributeRef;
	}

	@Override
	public boolean match(IStream stream) {
		return false;
	}
}
