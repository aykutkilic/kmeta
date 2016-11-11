package com.kilic.kmeta.core.alls.tn;

import java.util.Set;

public abstract class EdgeBase <S extends IState<?,?>> implements IEdge<S> {
	private S from, to;
	
	@Override
	public S getFrom() {
		return from;
	}
	
	@Override
	public void setFrom(final S state) {
		from = state;
	}
	
	@Override
	public S getTo() {
		return to;
	}
	
	@Override
	public void setTo(final S state) {
		to = state;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void connect(final S from, final S to) {
		this.from = from;
		this.to = to;
		((Set<IEdge<S>>) this.from.getOut()).add(this);
		((Set<IEdge<S>>) this.to.getIn()).add(this);
	}
	
	@Override
	public void disconnect() {
		this.from.getOut().remove(this);
		this.to.getIn().remove(this);
	}
	
	@Override
	public String toString() {
		return getFrom().getKey() + "-" + getLabel() + "->" + getTo().getKey();
	}
}
