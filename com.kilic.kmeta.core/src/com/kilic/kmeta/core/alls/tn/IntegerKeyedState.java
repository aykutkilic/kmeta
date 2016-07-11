package com.kilic.kmeta.core.alls.tn;

public class IntegerKeyedState<E extends IEdge<?>, S extends IState<Integer,?>> extends StateBase<Integer,E,S> {
	private static int stateIndexCounter = 0;

	protected IntegerKeyedState(ITransitionNetwork<Integer, S> container) {
		super(container, stateIndexCounter++);
	}
	
	@Override
	public int hashCode() {
		return key;
	}
}