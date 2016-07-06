package com.kilic.kmeta.core.parser;

import com.kilic.kmeta.core.analysis.BasicATNSimulator;
import com.kilic.kmeta.core.atn.ATN;
import com.kilic.kmeta.core.atn.ATNCallEdge;
import com.kilic.kmeta.core.atn.ATNMutatorEdge;
import com.kilic.kmeta.core.atn.ATNPredicateEdge;
import com.kilic.kmeta.core.atn.ATNState;
import com.kilic.kmeta.core.atn.ATNCharSetEdge;
import com.kilic.kmeta.core.atn.ATNEpsilonEdge;
import com.kilic.kmeta.core.atn.GSS;
import com.kilic.kmeta.core.atn.IATNEdge;
import com.kilic.kmeta.core.atn.RegularCallStack;
import com.kilic.kmeta.core.atn.ATNStringEdge;
import com.kilic.kmeta.core.stream.IStream;

/**
 * Algorithm from:
 * 
 * PARR T., HARWELL S., FISHER K. 
 * Adaptive LL(*) Parsing: The Power of Dynamic Analysis
 */
public class ALLSParser {
	public void parse( ATN atn, IStream input ) {
		ATNState p = atn.getStartState();
		ATNState oldp = p;
		
		while(true) {
			if( p == atn.getFinalState() )
				return;
			// else pop stacks and update p
			
			if( p.hasNext() ) {
				IATNEdge e = p.nextEdge();
				
				if(e instanceof ATNEpsilonEdge) {
					p = e.getTo();
				} else if(e instanceof ATNCallEdge) {
					ATNCallEdge ace = (ATNCallEdge)e;
					// push q
					p = ace.getATN().getStartState();
				} else if(e instanceof ATNMutatorEdge) {
					ATNMutatorEdge ame = (ATNMutatorEdge)e;
					p = ame.getTo();
				} else if(e instanceof ATNPredicateEdge) {
					ATNPredicateEdge ape = (ATNPredicateEdge)e;
					//error if !ape.evaluatePredicate();
					p = ape.getTo();
				} else if(e instanceof ATNCharSetEdge) {
					ATNCharSetEdge cse = (ATNCharSetEdge)e;
					// if not match error
					p = cse.getTo();
				} else if(e instanceof ATNStringEdge) {
					ATNStringEdge se = (ATNStringEdge)e;
					// if not match error
					p = se.getTo();
				}
			} else if( p.isDecisionState() ) {
				BasicATNSimulator slas = new BasicATNSimulator(input);
				p = slas.adaptivePredict(p, new RegularCallStack());
			} else {
				// error.
				return;
			}
			
			assert(p!=oldp);
			oldp = p;
		}
	}
}
