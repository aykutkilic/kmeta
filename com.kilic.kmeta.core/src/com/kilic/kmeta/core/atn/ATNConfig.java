package com.kilic.kmeta.core.atn;

import com.kilic.kmeta.core.dfa.DFAState;

public class ATNConfig {
	DFAState state;
	int alternative;
	GSSNode callStack;
}
