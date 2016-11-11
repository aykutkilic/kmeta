package com.kilic.kmeta.core.syntax;

import com.kilic.kmeta.core.alls.atn.ATN;
import com.kilic.kmeta.core.alls.atn.ATNState;

public interface ISyntaxExpr {
	ATNState appendToATN(final ATN atn, final ATNState sourceState, final ATNState targetState);
}
