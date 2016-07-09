package com.kilic.kmeta.core.alls.tn;

public abstract class EdgeBase <K> implements IEdge<K> {
	IState<K> from, to;
	
	@Override
	public IState<K> getFrom() {
		return from;
	}
	
	@Override
	public IState<K> getTo() {
		return to;
	}
	
	@Override
	public String toString() {
		return getFrom().getKey() + "-" + getLabel() + "->" + getTo().getKey();
	}
}
