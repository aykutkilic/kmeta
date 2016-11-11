package com.kilic.kmeta.core.alls.parser;

import java.lang.reflect.Field;

public class AssignCurrentRetValToFieldMutator implements IMutator {
	final String fieldName;
	Class<?> clazz;
	Field field;

	public AssignCurrentRetValToFieldMutator(final String fieldName) {
		this.fieldName = fieldName;
	}

	@Override
	public String getLabel() {
		return '=' + fieldName;
	}

	@Override
	public void run(final IParserContext context) {
		final POJOParserContext pojoCtx = (POJOParserContext) context;
		try {
			final Object obj = pojoCtx.getLocalObject();
			if (obj == null) {
				System.out.println("No local object to add to list for field " + fieldName);
				return;
			}

			if (field == null) {
				clazz = obj.getClass();
				field = clazz.getField(fieldName);
			}

			setValue(obj, pojoCtx.getReturnedObject());
		} catch (Exception e) {
			System.out.println("ERROR: can not set field " + fieldName + " of obj : " + clazz.getSimpleName());
		}
	}

	protected void setValue(final Object obj, final Object newValue) throws Exception {
		field.set(obj, newValue);
	}
}
