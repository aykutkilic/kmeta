package com.kilic.kmeta.core.atn;

public class ATNCallEdge extends ATNEdgeBase {
	ATN atn;

	ATNCallEdge(ATN atn) {
		this.atn = atn;
		atn.addCaller(this);
	}
	
	public ATN getATN() {
		return atn;
	}

	@Override
	public String getLabel() {
		if(atn.getLabel()!=null)
			return "[" + atn.getLabel() + "]";
		
		return "[??]"; 
	}
}
