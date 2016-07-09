package com.kilic.kmeta.core.tn;

public class IntegerKeyedState extends StateBase<Integer> {
	private static int stateIndexCounter = 0;

	protected IntegerKeyedState(ITransitionNetwork<Integer> container) {
		super(container, stateIndexCounter++);
	}
	
	@Override
	public int hashCode() {
		return key;
	}
}
