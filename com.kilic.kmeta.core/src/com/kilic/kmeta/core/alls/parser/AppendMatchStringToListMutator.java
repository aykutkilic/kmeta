package com.kilic.kmeta.core.alls.parser;

import java.util.List;

public class AppendMatchStringToListMutator extends AssignMatchStringToFieldMutator {
	public AppendMatchStringToListMutator(final String fieldName) {
		super(fieldName);
	}

	@Override
	public String getLabel() {
		return "+=" + fieldName;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void setValue(final Object obj, final Object newValue) throws Exception {
		final Object listObj = field.get(obj);
		if (listObj instanceof List<?>) {
			@SuppressWarnings("rawtypes")
			final List list = (List) listObj;
			list.add(newValue);
		} else {
			System.out.println("ERROR: field " + fieldName + " of obj : " + clazz.getSimpleName() + "is not a list");
		}
	}
}
