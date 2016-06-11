package com.kilic.kmeta.core.syntax;

import com.kilic.kmeta.core.automaton.Automaton;
import com.kilic.kmeta.core.automaton.AutomatonState;

public interface ISyntaxExpr {
	AutomatonState appendToNFA(Automaton dfa, AutomatonState sourceState, AutomatonState targetState);
}
