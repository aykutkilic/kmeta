package com.kilic.kmeta.core.alls.parser;

public class SetFieldMutator implements IMutator {
	String fieldName;

	public SetFieldMutator(String fieldName) {
		this.fieldName = fieldName;
	}

	@Override
	public String getLabel() {
		return '=' + fieldName;
	}

	@Override
	public void run(ParserContext context) {
	}
}
