package com.kilic.kmeta.core.atn;

import com.kilic.kmeta.core.dfa.AutomatonRunState;
import com.kilic.kmeta.core.dfa.AutomatonState;

public class ATNConfig {
	AutomatonState state;
	int alternative;
	AutomatonRunState callStack;
}
