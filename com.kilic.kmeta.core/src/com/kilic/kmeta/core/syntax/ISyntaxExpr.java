package com.kilic.kmeta.core.syntax;

import com.kilic.kmeta.core.atn.ATN;
import com.kilic.kmeta.core.atn.IATNState;

public interface ISyntaxExpr {
	IATNState appendToATN(ATN atn, IATNState sourceState, IATNState targetState);
}
