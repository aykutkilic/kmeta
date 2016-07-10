package com.kilic.kmeta.core.alls.tn;

import java.util.HashMap;
import java.util.Map;

public abstract class TransitionNetworkBase <K,E extends EdgeBase<S>, S extends IState<K,E>> implements ITransitionNetwork<K,S> {
	protected String label = "";
	protected Map<K, S> states;
	
	public TransitionNetworkBase() {
		states = new HashMap<>();
	}
	
	public String getLabel() {
		return label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	public boolean hasState(K key) {
		return states.containsKey(key);
	}
	
	public S getState(K key) {
		return states.get(key);
	}
	
	protected void connectEdge(S from, S to, E edge) {
		edge.from = from;
		edge.to = to;
		from.getOut().add(edge);
		to.getIn().add(edge);
	}
}
