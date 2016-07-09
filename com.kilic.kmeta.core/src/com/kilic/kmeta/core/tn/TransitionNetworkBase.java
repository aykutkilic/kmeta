package com.kilic.kmeta.core.tn;

import java.util.HashMap;
import java.util.Map;

import com.kilic.kmeta.core.atn.ATNEdgeBase;
import com.kilic.kmeta.core.atn.ATNState;

public abstract class TransitionNetworkBase <K> implements ITransitionNetwork<K> {
	protected String label = "";
	protected Map<K, IState<K>> states;
	
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
	
	public IState<K> getState(K key) {
		return states.get(key);
	}
	
	protected void connectEdge(StateBase<K> from, StateBase<K> to, EdgeBase<K> edge) {
		edge.from = from;
		edge.to = to;
		from.addOut(edge);
		to.addIn(edge);
	}
}
