package com.kilic.kmeta.core.alls.tn;

public interface ITransitionNetwork <K, S extends IState<K,?>> extends ILabeled {
	String getLabel();
	void setLabel(String label);
	
	boolean hasState(K key);
	S getState(K key);
}
