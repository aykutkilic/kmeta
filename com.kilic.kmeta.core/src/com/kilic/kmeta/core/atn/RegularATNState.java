package com.kilic.kmeta.core.atn;

import java.util.Set;

public class RegularATNState extends ATNStateBase {
	Set<IATNEdge> in;
	IATNEdge out;
	boolean isFinal;

	RegularATNState(boolean isFinal) {
		super();
		this.isFinal = isFinal;
	}
	
	@Override
	public void addIn(IATNEdge edge) {
		in.add(edge);
	}
	
	@Override
	public void addOut(IATNEdge edge) {
		assert(out == null);
		assert(!isFinal);
		
		out = edge;
	}
	
	public IATNState getNext() {
		if(out!=null)
			return out.getTo();
		
		return null;
	}
	
	@Override
	public String toString() {
		if(isFinal)
			return "[[" + getStateIndex() + "]]";
		
		return super.toString();
	}
}
