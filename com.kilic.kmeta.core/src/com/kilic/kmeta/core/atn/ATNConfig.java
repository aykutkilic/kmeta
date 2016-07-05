package com.kilic.kmeta.core.atn;

public class ATNConfig {
	ATNState state;
	int alternative;
	GSSNode callStack;
	
	public ATNConfig(ATNState state, int alternative, GSSNode callStack) {
		this.state = state;
		this.alternative = alternative;
		this.callStack = callStack;
	}
	
	public ATNState getState() { return state; }
	public int getAlternative() { return alternative; }
	public GSSNode getCallStack() { return callStack; }
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		
		result.append("[ ");
		result.append(state);
		result.append(", ");
		result.append(alternative);
		result.append(", ");
		result.append(callStack);
		result.append(" ]");
		
		return result.toString();
	}
}
