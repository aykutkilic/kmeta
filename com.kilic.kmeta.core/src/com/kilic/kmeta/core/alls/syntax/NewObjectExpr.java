package com.kilic.kmeta.core.alls.syntax;

import com.kilic.kmeta.core.alls.atn.ATN;
import com.kilic.kmeta.core.alls.atn.ATNState;
import com.kilic.kmeta.core.alls.parser.CreateObjectMutator;

public class NewObjectExpr implements ISyntaxExpr {
	String className;
	
	CreateObjectMutator mutator;
	
	public NewObjectExpr(String className) {
		this.className = className;
	}
	
	@Override
	public ATNState appendToATN(ATN atn, ATNState sourceState, ATNState targetState) {

		return null;
	}

}
