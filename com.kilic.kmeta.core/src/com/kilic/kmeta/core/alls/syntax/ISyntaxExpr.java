package com.kilic.kmeta.core.alls.syntax;

import com.kilic.kmeta.core.alls.atn.ATN;
import com.kilic.kmeta.core.alls.atn.ATNState;

public interface ISyntaxExpr {
	ATNState appendToATN(ATN atn, ATNState sourceState, ATNState targetState);
}
