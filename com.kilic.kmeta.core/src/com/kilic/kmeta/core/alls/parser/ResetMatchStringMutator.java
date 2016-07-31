package com.kilic.kmeta.core.alls.parser;

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
