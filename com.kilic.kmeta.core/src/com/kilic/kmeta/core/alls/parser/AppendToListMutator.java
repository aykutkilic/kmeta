package com.kilic.kmeta.core.alls.parser;

public class AppendToListMutator extends SetFieldMutator {
	public AppendToListMutator(String fieldName) {
		super(fieldName);
	}

	@Override
	public String getLabel() {
		return "+=" + fieldName;
	}

	@Override
	public void run(ParserContext context) {
	}
}
