package com.kilic.kmeta.core.alls.tn;

import java.util.Set;

public interface IState <K> extends ILabeled {
	enum StateType {
		REGULAR,
		FINAL,
		ERROR
	}

	K getKey();
	
	Set<IEdge<K>> getIn();
	Set<IEdge<K>> getOut();
	
	StateType getType();
	void setType(StateType newType);
}
