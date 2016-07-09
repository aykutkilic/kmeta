package com.kilic.kmeta.core.alls.analysis;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

import com.kilic.kmeta.core.alls.atn.ATNState;

public class RegularCallStack {
	public static final int ANY_STACK = -1;
	Stack<Integer> callStack;
	Map<Integer, ATNState> states;

	public static RegularCallStack newAnyStack() {
		RegularCallStack result = new RegularCallStack();
		result.pushAny();
		return result;
	}
	
	public RegularCallStack() {
		callStack = new Stack<>();
		states = new HashMap<>();
	}
	
	public RegularCallStack(RegularCallStack other) {
		this.callStack = new Stack<>();
		this.callStack.addAll(other.callStack);
		this.states = new HashMap<>(other.states);
	}
	
	public void pushAny() {
		callStack.push(ANY_STACK);
	}
	
	public boolean isAny() {
		return callStack.peek() == ANY_STACK;
	}
	
	public void push(ATNState state) {
		callStack.push(state.getKey());
		states.put(state.getKey(), state);
	}
	
	public ATNState pop() {
		int stateIndex = callStack.pop();
		
		ATNState result = states.get(stateIndex);
		states.remove(stateIndex);
		
		return result;
	}
	
	public ATNState peek() {
		if(callStack.isEmpty()) return null;
		return states.get(callStack.peek());
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		
		result.append("<");
		Iterator<Integer> i = callStack.iterator();
		while(i.hasNext()) {
			Integer index = i.next();
			result.append( index!=ANY_STACK ? index : "#" );
			if(i.hasNext())
				result.append(",");
		}
		result.append(">");
		
		return result.toString();
	}
}
