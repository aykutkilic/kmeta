package com.kilic.kmeta.core.alls.parser;

public class CreateObjectMutator implements IMutator {
	String className;

	public CreateObjectMutator(String className) {
		this.className = className;
	}

	@Override
	public String getLabel() {
		return '{' + className + '}';
	}
	
	@Override
	public void run(ParserContext context) {
	}
}
