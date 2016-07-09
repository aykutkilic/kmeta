package com.kilic.kmeta.core.tn;

public interface ITransitionNetwork <K> extends ILabeled {
	String getLabel();
	void setLabel(String label);
	
	boolean hasState(K key);
	IState<K> getState(K key);
}
