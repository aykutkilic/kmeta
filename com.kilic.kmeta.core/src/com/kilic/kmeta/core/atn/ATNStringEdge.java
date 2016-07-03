package com.kilic.kmeta.core.atn;

public class ATNStringEdge extends ATNEdgeBase {
	String string;

	ATNStringEdge(String string) {
		this.string = string;
	}

	@Override
	public String getLabel() {
		return "'" + string +  "'";
	}
}
