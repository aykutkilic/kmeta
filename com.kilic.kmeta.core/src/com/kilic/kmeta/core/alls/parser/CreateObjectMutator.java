package com.kilic.kmeta.core.alls.parser;

public class CreateObjectMutator implements IMutator {
	final String className;

	public CreateObjectMutator(final String className) {
		this.className = className;
	}

	@Override
	public String getLabel() {
		return '{' + className + '}';
	}

	@Override
	public void run(final IParserContext context) {
		final POJOParserContext pojoCtx = (POJOParserContext) context;
		final Class<?> clazz = pojoCtx.getClass(className);
		if (clazz == null) {
			System.out.print("ERROR - can not find class " + className);
			return;
		}
		try {
			final Object newObject = clazz.newInstance();
			pojoCtx.setLocalObject(newObject);
		} catch (InstantiationException | IllegalAccessException e) {
			System.out.print("ERROR - can not instantiate class " + className);
		}
	}
}
