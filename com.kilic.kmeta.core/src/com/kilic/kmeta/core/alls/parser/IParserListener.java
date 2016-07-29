package com.kilic.kmeta.core.alls.parser;

import com.kilic.kmeta.core.alls.atn.ATN;

public interface IParserListener {
	void onCall(ATN atn);
	void onReturn(ATN atn);

	void onChar(char c);
	void onFinished();
}
