package com.kilic.kmeta.core.alls.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.kilic.kmeta.core.alls.atn.ATN;

public class POJOParserContext implements IParserContext {
	final private List<String> javaPackages;
	final private Map<String, Class<?>> classFromShortName;
	final private Stack<Object> callStack;

	private Object returnedObject;
	private StringBuilder lastMatch = new StringBuilder();
	private IMutator assignNext = null;

	public POJOParserContext() {
		javaPackages = new ArrayList<>();
		classFromShortName = new HashMap<>();
		callStack = new Stack<>();
	}

	public Object getReturnedObject() {
		return returnedObject;
	}

	public void setReturnedObject(final Object value) {
		returnedObject = value;
	}

	public Object getLocalObject() {
		return callStack.peek();
	}

	public void setLocalObject(final Object newObject) {
		callStack.pop();
		callStack.push(newObject);
	}

	public void addJavaPackage(final String name) {
		javaPackages.add(name);
	}

	public Class<?> getClass(final String name) {
		if (classFromShortName.containsKey(name))
			return classFromShortName.get(name);

		for (String javaPackage : javaPackages) {
			try {
				Class<?> clazz = Class.forName(javaPackage + "." + name);
				classFromShortName.put(name, clazz);
				return clazz;
			} catch (ClassNotFoundException e) {
			}
		}

		return null;
	}

	public void resetMatchString() {
		lastMatch = new StringBuilder();
	}

	public String getMatchString() {
		return lastMatch.toString();
	}

	@Override
	public void onCall(final ATN atn) {
		callStack.push(null);
		resetMatchString();
	}

	@Override
	public void onReturn(final ATN atn) {
		final Object currentVal = callStack.pop();
		if (currentVal != null)
			returnedObject = currentVal;
	}

	@Override
	public void onChar(final char c) {
		lastMatch.append(c);
	}

	@Override
	public void onMutator(IMutator mutator) {
		mutator.run(this);
	}

	@Override
	public void onFinished() {
	}
}
