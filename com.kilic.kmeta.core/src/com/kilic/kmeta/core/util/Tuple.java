package com.kilic.kmeta.core.util;

public class Tuple<A, B> {
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
	public boolean equals(final Object other) {
		if (other instanceof Tuple<?, ?>) {
			Tuple<?, ?> t = (Tuple<?, ?>) other;
			return a.equals(t.a) && b.equals(t.b);
		}

		return false;
	}
}
