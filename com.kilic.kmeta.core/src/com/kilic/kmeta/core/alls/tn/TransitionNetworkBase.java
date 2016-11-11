package com.kilic.kmeta.core.alls.tn;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class TransitionNetworkBase<K, E extends IEdge<S>, S extends IState<K, E>>
		implements ITransitionNetwork<K, S> {
	protected final Map<K, S> states;

	protected String label = "";

	public TransitionNetworkBase() {
		states = new HashMap<>();
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(final String label) {
		this.label = label;
	}

	public boolean hasState(final K key) {
		return states.containsKey(key);
	}

	public S getState(final K key) {
		return states.get(key);
	}

	public Collection<S> getStates() {
		return states.values();
	}
}
