package com.kilic.kmeta.core.alls.parser;

import com.kilic.kmeta.core.alls.atn.ATN;

public interface IParserContext {
	void onCall(ATN atn);
	void onReturn(ATN atn);

	void onChar(char c);
	void onMutator(IMutator mutator);
	
	void onFinished();
}
