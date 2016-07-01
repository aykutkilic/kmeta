package com.kilic.kmeta.core.syntax;

import com.kilic.kmeta.core.atn.ATN;
import com.kilic.kmeta.core.atn.ATNState;
import com.kilic.kmeta.core.dfa.Automaton;
import com.kilic.kmeta.core.dfa.AutomatonState;

public interface ISyntaxExpr {
	ATNState appendToATN(ATN atn, ATNState sourceState, ATNState targetState);

	AutomatonState appendToNFA(Automaton dfa, AutomatonState sourceState, AutomatonState targetState);
}
