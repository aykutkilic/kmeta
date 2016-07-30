package com.kilic.kmeta.core.alls.parser;

import com.kilic.kmeta.core.alls.tn.ILabeled;

public interface IMutator extends ILabeled {
	void run(ParserContext context);
}
