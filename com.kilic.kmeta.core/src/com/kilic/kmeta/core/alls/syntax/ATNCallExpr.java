package com.kilic.kmeta.core.alls.syntax;

import com.kilic.kmeta.core.alls.atn.ATN;
import com.kilic.kmeta.core.alls.atn.ATNState;

public class ATNCallExpr implements ISyntaxExpr {
	ATN calledATN;
	
	public ATNCallExpr(ATN atn) {
		this.calledATN = atn;
	}
	
	@Override
	public ATNState appendToATN(ATN atn, ATNState sourceState, ATNState targetState) {
		if(targetState == null)
			targetState = atn.createState();
		
		atn.createCallEdge(sourceState, targetState, this.calledATN);
		
		return targetState;
	}
}
