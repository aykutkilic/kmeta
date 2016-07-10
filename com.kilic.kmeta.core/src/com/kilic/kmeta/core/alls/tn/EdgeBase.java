package com.kilic.kmeta.core.alls.tn;

public abstract class EdgeBase <S extends IState<?,?>> implements IEdge<S> {
	S from, to;
	
	@Override
	public S getFrom() {
		return from;
	}
	
	@Override
	public void setFrom(S state) {
		from = state;
	}
	
	@Override
	public S getTo() {
		return to;
	}
	
	@Override
	public void setTo(S state) {
		to = state;
	}
	
	@Override
	public String toString() {
		return getFrom().getKey() + "-" + getLabel() + "->" + getTo().getKey();
	}
}
