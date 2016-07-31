package com.kilic.kmeta.core.syntax;

import com.kilic.kmeta.core.alls.parser.IMutator;
import com.kilic.kmeta.core.alls.parser.IParserContext;

public class ResetMatchStringMutator implements IMutator {
	@Override
	public String getLabel() {
		return "RST";
	}

	@Override
	public void run(IParserContext context) {
		POJOParserContext pojoCtx = (POJOParserContext) context;
		pojoCtx.resetMatchString();
	}
}
