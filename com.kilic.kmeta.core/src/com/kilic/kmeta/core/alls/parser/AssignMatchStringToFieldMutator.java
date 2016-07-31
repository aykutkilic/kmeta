package com.kilic.kmeta.core.alls.parser;

import java.lang.reflect.Field;

public class AssignMatchStringToFieldMutator implements IMutator {
	String fieldName;
	Class<?> clazz;
	Field field;

	public AssignMatchStringToFieldMutator(String fieldName) {
		this.fieldName = fieldName;
	}

	@Override
	public String getLabel() {
		return "->" + fieldName;
	}

	@Override
	public void run(IParserContext context) {
		POJOParserContext pojoCtx = (POJOParserContext) context;
		try {
			Object obj = pojoCtx.getLocalObject();
			if (obj == null) {
				System.out.println("No local object");
				return;
			}

			if (field == null) {
				clazz = obj.getClass();
				field = clazz.getField(fieldName);
				if (!field.getType().isAssignableFrom(String.class)) {
					System.out.println("ERROR: can not set field " + fieldName + ". It is not assignable from string");
					return;
				}
			}

			setValue(obj, pojoCtx.getMatchString());
			pojoCtx.resetMatchString();
		} catch (Exception e) {
			System.out.println("ERROR: can not set field " + fieldName + " of obj : " + clazz.getSimpleName());
		}
	}
	
	protected void setValue(Object obj, Object newValue) throws Exception {
		field.set(obj, newValue);
	}
}
