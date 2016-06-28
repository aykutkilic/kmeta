package com.kilic.kmeta.core.atn;

public class ATNProduction {
	ATNState startState;
	ATNState finalState;

	ATN container;

	public ATNProduction(ATN container) {
		startState = new ATNState();
		finalState = new ATNState();
	}

	String toGraphviz() {
		StringBuilder result = new StringBuilder();

		return result.toString();
	}
}
