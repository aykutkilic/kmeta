package com.kilic.kmeta.core.syntax;

import com.kilic.kmeta.core.atn.ATN;
import com.kilic.kmeta.core.atn.ATNState;

public interface ISyntaxExpr {
	ATNState appendToATN(ATN atn, ATNState sourceState, ATNState targetState);
}
