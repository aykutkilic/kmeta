package com.kilic.kmeta.core.alls.tn;

import java.util.HashSet;
import java.util.Set;

public class StateBase<K, E extends IEdge<?>, S extends IState<K,?>> implements IState<K,E> {
	protected K key;
	protected ITransitionNetwork<K, S> container;
	protected StateType type;

	protected Set<E> in, out;
	protected String label = "";
	
	protected StateBase(ITransitionNetwork<K, S> container, K key) {
		in = new HashSet<>();
		out = new HashSet<>();

		this.container = container;
		this.key = key;
	}

	public ITransitionNetwork<K,S> getContainer() {
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
	public void setType(StateType newType) {
		this.type = newType;
	}
	
	protected boolean isFinal() {
		return getType()==StateType.FINAL;
	}

	public K getKey() {
		return key;
	}

	public void addIn(E edge) {
		in.add(edge);
	}

	public void addOut(E edge) {
		out.add(edge);
	}

	@Override
	public boolean equals(Object other) {
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
