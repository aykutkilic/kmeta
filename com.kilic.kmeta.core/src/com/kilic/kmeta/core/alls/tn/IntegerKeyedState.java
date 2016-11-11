package com.kilic.kmeta.core.alls.tn;

public class IntegerKeyedState<E extends IEdge<?>, S extends IState<Integer,?>> extends StateBase<Integer,E,S> {
	private static int stateIndexCounter = 0;

	protected IntegerKeyedState(final ITransitionNetwork<Integer, S> container) {
		super(container, stateIndexCounter++);
		label = String.valueOf(key);
	}
	
	@Override
	public int hashCode() {
		return key;
	}
}
