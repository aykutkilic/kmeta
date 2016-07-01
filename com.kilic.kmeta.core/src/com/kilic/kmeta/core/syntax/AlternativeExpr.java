package com.kilic.kmeta.core.syntax;

import java.util.ArrayList;
import java.util.List;

import com.kilic.kmeta.core.dfa.DFA;
import com.kilic.kmeta.core.dfa.DFAState;

public class AlternativeExpr implements ISyntaxExpr {
	List<ISyntaxExpr> alternatives = new ArrayList<>();

	public AlternativeExpr(ISyntaxExpr... exprs) {
		for (ISyntaxExpr e : exprs)
			alternatives.add(e);
	}

	public void addAlternative(ISyntaxExpr alternative) {
		alternatives.add(alternative);
	}

	public ISyntaxExpr[] getAlternatives() {
		return (ISyntaxExpr[]) alternatives.toArray();
	}
}
