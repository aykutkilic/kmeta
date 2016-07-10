package com.kilic.kmeta.core.alls.tn;

public abstract class EdgeBase <S extends IState<?,?>> implements IEdge<S> {
	S from, to;
	
	@Override
	public S getFrom() {
		return from;
	}
	
	@Override
	public S getTo() {
		return to;
	}
	
	@Override
	public String toString() {
		return getFrom().getKey() + "-" + getLabel() + "->" + getTo().getKey();
	}
}
