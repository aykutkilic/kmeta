package com.kilic.kmeta.core.atn;

import com.kilic.kmeta.core.dfa.AutomatonState;

public class ATNConfig {
	AutomatonState state;
	int alternative;
	GSSNode callStack;
}
