package com.kilic.kmeta.core.syntax;

import com.kilic.kmeta.core.dfa.DFA;
import com.kilic.kmeta.core.dfa.DFAState;

public interface ISyntaxExpr {
	DFAState appendToDFA(DFA dfa, DFAState sourceState, DFAState targetState);
}
