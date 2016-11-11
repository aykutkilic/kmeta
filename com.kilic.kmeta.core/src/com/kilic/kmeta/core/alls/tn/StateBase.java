package com.kilic.kmeta.core.alls.tn;

import java.util.HashSet;
import java.util.Set;

public class StateBase<K, E extends IEdge<?>, S extends IState<K,?>> implements IState<K,E> {
	protected final K key;
	protected final ITransitionNetwork<K, S> container;
	protected final Set<E> in, out;
	
	protected String label = "";
	protected StateType type;
	
	protected StateBase(final ITransitionNetwork<K, S> container, final K key) {
		in = new HashSet<>();
		out = new HashSet<>();

		this.container = container;
		this.key = key;
		type = StateType.REGULAR;
	}

	public ITransitionNetwork<K,S> getContainer() {
		return container;
	}

	@Override
	public String getLabel() {
		return label;
	}
	
	public void setLabel(final String label) {
		this.label = label;
	}
	
	@Override
	public Set<E> getIn() {
		return in;
	}

	@Override
	public Set<E> getOut() {
		return out;
	}

	@Override
	public StateType getType() {
		return type;
	}

	@Override
	public void setType(final StateType newType) {
		this.type = newType;
	}
	
	protected boolean isFinal() {
		return getType()==StateType.FINAL;
	}

	public K getKey() {
		return key;
	}

	public void addIn(final E edge) {
		in.add(edge);
	}

	public void addOut(final E edge) {
		out.add(edge);
	}

	@Override
	public boolean equals(final Object other) {
		if (this == other)
			return true;

		if (!(other instanceof StateBase<?,?,?>))
			return false;

		if (type != ((StateBase<?,?,?>) other).type)
			return false;

		return key.equals(((StateBase<?,?,?>) other).key);
	}

	@Override
	public String toString() {
		switch (type) {
		case FINAL:
			return "[[" + key.toString() + "]]";
			
		case ERROR:
			return "[E " + key.toString() + " ]";

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
