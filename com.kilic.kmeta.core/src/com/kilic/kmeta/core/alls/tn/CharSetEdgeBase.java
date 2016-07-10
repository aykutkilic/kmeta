package com.kilic.kmeta.core.alls.tn;

import com.kilic.kmeta.core.alls.stream.IStream;
import com.kilic.kmeta.core.util.CharSet;

public abstract class CharSetEdgeBase <S extends IState<?,?>> extends EdgeBase<S> {
	CharSet charSet;

	protected CharSetEdgeBase(CharSet charSet) {
		this.charSet = charSet;
	}

	@Override
	public String getLabel() {
		return charSet.toString();
	}

	public CharSet getCharSet() {
		return charSet;
	}
	
	@Override
	public boolean moves(IStream input) {
		char c = input.lookAheadChar(0);
		return charSet.containsSingleton(c);
	}

	@Override
	public int hashCode() {
		return charSet.hashCode();
	}
	
	@Override
	public boolean equals(Object other) {
		if( this == other )
			return true;
		
		if( !(other instanceof CharSetEdgeBase<?>))
			return false;
		
		return charSet.equals(((CharSetEdgeBase<?>)other).charSet);
	}
}
