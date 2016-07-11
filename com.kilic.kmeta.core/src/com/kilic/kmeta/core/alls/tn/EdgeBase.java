package com.kilic.kmeta.core.alls.tn;

import java.util.Set;

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
	
	@SuppressWarnings("unchecked")
	@Override
	public void connect(S from, S to) {
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
