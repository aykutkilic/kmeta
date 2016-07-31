package com.kilic.kmeta.core.syntax;

import java.util.List;

public class AppendCurrentRetValToListMutator extends AssignCurrentRetValToField {
	public AppendCurrentRetValToListMutator(String fieldName) {
		super(fieldName);
	}

	@Override
	public String getLabel() {
		return "+=" + fieldName;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void setValue(Object obj, Object newValue) throws Exception {
		Object listObj = field.get(obj);
		if (listObj instanceof List<?>) {
			@SuppressWarnings("rawtypes")
			List list = (List) listObj;
			list.add(newValue);
		} else {
			System.out.println("ERROR: field " + fieldName + " of obj : " + clazz.getSimpleName() + "is not a list");
		}
	}
}
