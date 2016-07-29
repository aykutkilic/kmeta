package com.kilic.kmeta.core.alls.parser;

import com.kilic.kmeta.core.alls.analysis.BasicATNSimulator;
import com.kilic.kmeta.core.alls.analysis.RegularCallStack;
import com.kilic.kmeta.core.alls.atn.ATN;
import com.kilic.kmeta.core.alls.atn.ATNCallEdge;
import com.kilic.kmeta.core.alls.atn.ATNCharSetEdge;
import com.kilic.kmeta.core.alls.atn.ATNEpsilonEdge;
import com.kilic.kmeta.core.alls.atn.ATNMutatorEdge;
import com.kilic.kmeta.core.alls.atn.ATNPredicateEdge;
import com.kilic.kmeta.core.alls.atn.ATNState;
import com.kilic.kmeta.core.alls.atn.IATNEdge;
import com.kilic.kmeta.core.alls.stream.IStream;

/**
 * Algorithm from:
 * 
 * PARR T., HARWELL S., FISHER K. Adaptive LL(*) Parsing: The Power of Dynamic
 * Analysis
 */
public class ALLSParser {
	IParserListener listener;
	
	public IParserListener getListener() {
		return listener;
	}
	
	public void setListener(IParserListener listener) {
		this.listener = listener;
	}
	
	
	public void parse(ATN atn, IStream input) {
		ATNState p = atn.getStartState();
		RegularCallStack callStack = new RegularCallStack();
		ATNState oldp = p;

		if(listener!=null)
			listener.onCall(atn);
		
		while (true) {
			if (p == atn.getFinalState()) {
				if(listener!=null)
					listener.onFinished();
				return;
			}

			if (p.hasNext()) {
				IATNEdge e = p.nextEdge();

				if (e instanceof ATNEpsilonEdge) {
					p = e.getTo();
				} else if (e instanceof ATNCallEdge) {
					ATNCallEdge ace = (ATNCallEdge) e;
					callStack.push(e.getTo());
					p = ace.getATN().getStartState();
					if(listener!=null)
						listener.onCall(ace.getATN());
				} else if (e instanceof ATNMutatorEdge) {
					ATNMutatorEdge ame = (ATNMutatorEdge) e;
					p = ame.getTo();
				} else if (e instanceof ATNPredicateEdge) {
					ATNPredicateEdge ape = (ATNPredicateEdge) e;
					p = ape.getTo();
				} else if (e instanceof ATNCharSetEdge) {
					ATNCharSetEdge cse = (ATNCharSetEdge) e;
					char c = input.nextChar();
					if(listener!=null)
						listener.onChar(c);
					p = cse.getTo();
				}
			} else if (p.isFinalState()) {
				if (callStack.isEmpty()) {
					System.out.println("ERROR! empty callstack");
					assert(false);
					return;
				}
				
				if(listener!=null)
					listener.onReturn((ATN) p.getContainer());
				
				p = callStack.pop();
			} else if (p.isDecisionState()) {
				BasicATNSimulator slas = new BasicATNSimulator(input);
				IATNEdge predictedEdge = slas.adaptivePredict(p, callStack);
				p = predictedEdge.getTo();
			} else {
				System.out.println("ATN ERROR! " + p);
				assert(false);
				return;
			}

			assert (p != null && p != oldp);
			oldp = p;
		}
	}
}
