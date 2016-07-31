package com.kilic.kmeta.core.syntax;

import com.kilic.kmeta.core.alls.parser.IMutator;
import com.kilic.kmeta.core.alls.parser.IParserContext;

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
	public void run(IParserContext context) {
		POJOParserContext pojoCtx = (POJOParserContext) context;
		Class<?> clazz = pojoCtx.getClass(className);
		if (clazz == null) {
			System.out.print("ERROR - can not find class " + className);
			return;
		}
		try {
			Object newObject = clazz.newInstance();
			pojoCtx.setLocalObject(newObject);
		} catch (InstantiationException | IllegalAccessException e) {
			System.out.print("ERROR - can not instantiate class " + className);
		}
	}
}
