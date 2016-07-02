package com.kilic.kmeta.core.parser;

import com.kilic.kmeta.core.analysis.StepLockedATNSimulator;
import com.kilic.kmeta.core.atn.ATN;
import com.kilic.kmeta.core.atn.ATNCallEdge;
import com.kilic.kmeta.core.atn.ATNPredicateEdge;
import com.kilic.kmeta.core.atn.ATNState;
import com.kilic.kmeta.core.atn.CharSetEdge;
import com.kilic.kmeta.core.atn.EpsilonEdge;
import com.kilic.kmeta.core.atn.GSS;
import com.kilic.kmeta.core.atn.IATNEdge;
import com.kilic.kmeta.core.atn.StringEdge;
import com.kilic.kmeta.core.stream.IStream;

/**
 * Algorithm from:
 * 
 * PARR T., HARWELL S., FISHER K. 
 * Adaptive LL(*) Parsing: The Power of Dynamic Analysis
 */
public class ALLSParser {
	void parse(ATN atn, IStream input ) {
		GSS gss = new GSS();
		ATNState p = atn.getStartState();
		while(true) {
			if( p == atn.getFinalState() )
				return;
			// else pop stacks and update p
			
			if( p.hasNext() ) {
				IATNEdge e = p.nextEdge();
				
				if(e instanceof EpsilonEdge) {
					p = e.getTo();
				} else if(e instanceof ATNCallEdge) {
					ATNCallEdge ace = (ATNCallEdge)e;
					// push q
					p = ace.getATN().getStartState();
				} else if(e instanceof ATNPredicateEdge) {
					ATNPredicateEdge ape = (ATNPredicateEdge)e;
					//error if !ape.evaluatePredicate();
					p = ape.getTo();
				} else if(e instanceof CharSetEdge) {
					CharSetEdge cse = (CharSetEdge)e;
					// if not match error
					p = cse.getTo();
				} else if(e instanceof StringEdge) {
					StringEdge se = (StringEdge)e;
					// if not match error
					p = se.getTo();
				}
			} else if( p.isDecisionState() ) {
				StepLockedATNSimulator slas = new StepLockedATNSimulator(p, gss);
				p = slas.adaptivePredict();
			} else {
				// error.
				return;
			}
		}
	}
}
