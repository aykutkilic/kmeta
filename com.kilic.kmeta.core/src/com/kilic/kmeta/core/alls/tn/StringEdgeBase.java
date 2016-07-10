package com.kilic.kmeta.core.alls.tn;

import com.kilic.kmeta.core.alls.stream.IStream;

public abstract class StringEdgeBase <S extends IState<?,?>> extends EdgeBase<S> {
	String string;
	
	protected StringEdgeBase(String string) {
		assert(string.length()>=1);
		this.string = string;
	}

	@Override
	public String getLabel() {
		return string;
	}
	
	public String getString() {
		return string;
	}

	@Override
	public boolean moves(IStream input) {
		return string.equals(input.lookAheadString(0, string.length()));
	}
	
	@Override
	public int hashCode() {
		return string.hashCode();
	}
	
	@Override
	public boolean equals(Object other) {
		if( this == other )
			return true;
		
		if( !(other instanceof StringEdgeBase<?>))
			return false;
		
		return string.equals(((StringEdgeBase<?>)other).string);
	}
}
