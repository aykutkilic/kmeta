package com.kilic.kmeta.core.syntax;

import com.kilic.kmeta.core.automaton.AttributeRefMatcher;
import com.kilic.kmeta.core.automaton.Automaton;
import com.kilic.kmeta.core.automaton.AutomatonState;
import com.kilic.kmeta.core.meta.MAttribute;

public class AttributeRef implements ISyntaxExpr {
	MAttribute attribute;

	public AttributeRef(MAttribute attribute) {
		this.attribute = attribute;
	}

	public MAttribute getAttribute() {
		return attribute;
	}

	public void setAttribute(MAttribute attribute) {
		this.attribute = attribute;
	}

	@Override
	public AutomatonState appendToNFA(Automaton nfa, AutomatonState sourceState, AutomatonState targetState) {
		if (targetState == null)
			targetState = nfa.createState();

		nfa.createTransition(sourceState, targetState, new AttributeRefMatcher(this));

		return targetState;
	}

	@Override
	public String toString() {
		if (attribute == null)
			return "()";
		return "(" + attribute.getType() + ")";
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof AttributeRef)
			return this.attribute == ((AttributeRef) other).attribute;

		return false;
	}
}
