package com.kilic.kmeta.core.util;

public class Tuple<A, B> {
	private static final int PRIME = 31;

	private final A a;
	private final B b;

	public Tuple(final A a, final B b) {
		this.a = a;
		this.b = b;
	}

	public A getA() {
		return a;
	}

	public B getB() {
		return b;
	}

	@Override
	public int hashCode() {
		return a.hashCode() * PRIME + b.hashCode();
	}

	@Override
	public boolean equals(final Object that) {
		if (this == that)
			return true;

		if (that == null)
			return false;

		if (that instanceof Tuple<?, ?>) {
			final Tuple<?, ?> t = (Tuple<?, ?>) that;
			return a.equals(t.a) && b.equals(t.b);
		}

		return false;
	}
}
