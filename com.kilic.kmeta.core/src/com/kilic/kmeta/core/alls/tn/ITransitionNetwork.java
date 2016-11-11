package com.kilic.kmeta.core.alls.tn;

public interface ITransitionNetwork <K, S extends IState<K,?>> extends ILabeled {
	String getLabel();
	void setLabel(final String label);
	
	boolean hasState(final K key);
	S getState(final K key);
	S getStartState();
	
	String toGraphviz();
}
