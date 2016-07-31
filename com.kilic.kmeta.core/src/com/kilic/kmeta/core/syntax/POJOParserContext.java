package com.kilic.kmeta.core.syntax;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.kilic.kmeta.core.alls.atn.ATN;
import com.kilic.kmeta.core.alls.parser.IMutator;
import com.kilic.kmeta.core.alls.parser.IParserContext;

public class POJOParserContext implements IParserContext {
	List<String> javaPackages;
	Map<String, Class<?>> classFromShortName;
	Stack<Object> callStack;

	Object returnedObject;
	StringBuilder lastMatch = new StringBuilder();
	IMutator assignNext = null;

	public POJOParserContext() {
		javaPackages = new ArrayList<>();
		classFromShortName = new HashMap<>();
		callStack = new Stack<>();
	}

	public Object getReturnedObject() {
		return returnedObject;
	}

	public void setReturnedObject(Object value) {
		returnedObject = value;
	}

	public Object getLocalObject() {
		return callStack.peek();
	}

	public void setLocalObject(Object newObject) {
		callStack.pop();
		callStack.push(newObject);
	}

	public void addJavaPackage(String name) {
		javaPackages.add(name);
	}

	public Class<?> getClass(String name) {
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
	public void onCall(ATN atn) {
		callStack.push(null);
		resetMatchString();
	}

	@Override
	public void onReturn(ATN atn) {
		Object currentVal = callStack.pop();
		if (currentVal != null)
			returnedObject = currentVal;
	}

	@Override
	public void onChar(char c) {
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
