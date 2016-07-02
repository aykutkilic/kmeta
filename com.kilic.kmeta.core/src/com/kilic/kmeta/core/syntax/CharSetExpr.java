package com.kilic.kmeta.core.syntax;

import com.kilic.kmeta.core.atn.ATN;
import com.kilic.kmeta.core.atn.IATNState;
import com.kilic.kmeta.core.util.CharSet;

public class CharSetExpr implements ISyntaxExpr {
	CharSet charSet;

	public CharSetExpr(CharSet charSet) {
		this.charSet = charSet;
	}

	public CharSet getCharSet() {
		return charSet;
	}

	@Override
	public IATNState appendToATN(ATN atn, IATNState sourceState, IATNState targetState) {
		if (targetState == null)
			targetState = atn.createRegularState();

		atn.createCharSetEdge(sourceState, targetState, charSet);

		return targetState;
	}
}
