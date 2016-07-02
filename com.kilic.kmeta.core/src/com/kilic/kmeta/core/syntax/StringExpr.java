package com.kilic.kmeta.core.syntax;

import com.kilic.kmeta.core.atn.ATN;
import com.kilic.kmeta.core.atn.IATNState;

public class StringExpr implements ISyntaxExpr {
	String string;

	public StringExpr(String string) {
		this.string = string;
	}

	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}

	@Override
	public IATNState appendToATN(ATN atn, IATNState sourceState, IATNState targetState) {
		if (targetState == null)
			targetState = atn.createRegularState();

		atn.createStringEdge(sourceState, targetState, string);

		return targetState;
	}
}
