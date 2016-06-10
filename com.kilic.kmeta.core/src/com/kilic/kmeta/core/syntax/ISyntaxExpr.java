package com.kilic.kmeta.core.syntax;

import com.kilic.kmeta.core.dfa.Automaton;
import com.kilic.kmeta.core.dfa.AutomatonState;

public interface ISyntaxExpr {
	AutomatonState appendToNFA(Automaton dfa, AutomatonState sourceState, AutomatonState targetState);
}
