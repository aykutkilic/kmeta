package com.kilic.kmeta.core.parser;

import com.kilic.kmeta.core.atn.ATN;
import com.kilic.kmeta.core.atn.GSS;
import com.kilic.kmeta.core.atn.IATNState;
import com.kilic.kmeta.core.stream.IStream;

/**
 * Algorithm from:
 * 
 * PARR T., HARWELL S., FISHER K. 
 * Adaptive LL(*) Parsing: The Power of Dynamic Analysis
 */
public class ALLSParser {
	void parse(ATN atn, IStream input ) {
		// γ := []; i := adaptivePredict(S, γ); p := pS,i;
		GSS gss = new GSS();
		IATNState p = atn.getStartState();
		while(true) {
			if( p == atn.getFinalState() )
				return;
			// else pop stacks and update p
			
				
		}
	}
}
