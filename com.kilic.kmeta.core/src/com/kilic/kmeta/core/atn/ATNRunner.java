package com.kilic.kmeta.core.atn;

public class ATNRunner {
	ATN atn;
	GSS gss;
	
	ATNRunner(ATN atn) {
		this.atn = atn;
		gss = new GSS();
	}
	
	public void step() {
		
	}
}
