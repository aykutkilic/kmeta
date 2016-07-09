package com.kilic.kmeta.core.tn;

import java.util.HashSet;
import java.util.Set;

import com.kilic.kmeta.core.tn.IState.StateType;

public class StateBase<K> implements IState<K> {
	protected K key;
	protected ITransitionNetwork<K> container;
	protected StateType type;

	protected Set<IEdge<K>> in, out;
	protected String label = "";
	
	protected StateBase(ITransitionNetwork<K> container, K key) {
		in = new HashSet<>();
		out = new HashSet<>();

		this.container = container;
		this.key = key;
	}

	public ITransitionNetwork<K> getContainer() {
		return container;
	}

	@Override
	public String getLabel() {
		return label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	@Override
	public Set<IEdge<K>> getIn() {
		return in;
	}

	@Override
	public Set<IEdge<K>> getOut() {
		return out;
	}

	@Override
	public StateType getType() {
		return type;
	}

	@Override
	public void setType(StateType newType) {
		this.type = newType;
	}
	
	protected boolean isFinal() {
		return getType()==StateType.FINAL;
	}

	public K getKey() {
		return key;
	}

	public void addIn(IEdge<K> edge) {
		in.add(edge);
	}

	public void addOut(IEdge<K> edge) {
		out.add(edge);
	}

	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;

		if (!(other instanceof StateBase<?>))
			return false;

		if (type != ((StateBase<?>) other).type)
			return false;

		return key.equals(((StateBase<?>) other).key);
	}

	@Override
	public String toString() {
		switch (type) {
		case FINAL:
			return "[[" + key.toString() + "]]";
		case ERROR:
			return "[E " + key.toString() + " E]";

		case REGULAR:
		default:
			return "[" + key.toString() + "]";
		}
	}

	@Override
	public int hashCode() {
		return key.hashCode();
	}
}
