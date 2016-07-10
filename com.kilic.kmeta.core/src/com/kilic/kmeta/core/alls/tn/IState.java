package com.kilic.kmeta.core.alls.tn;

import java.util.Set;

public interface IState <K, E extends IEdge<?>> extends ILabeled {
	enum StateType {
		REGULAR,
		FINAL,
		ERROR
	}

	K getKey();
	
	Set<E> getIn();
	Set<E> getOut();
	
	StateType getType();
	void setType(StateType newType);
}
