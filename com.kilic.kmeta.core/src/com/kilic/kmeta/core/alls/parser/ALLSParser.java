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
import com.kilic.kmeta.core.alls.atn.ATNStringEdge;
import com.kilic.kmeta.core.alls.atn.ATNTokenEdge;
import com.kilic.kmeta.core.alls.atn.IATNEdge;
import com.kilic.kmeta.core.alls.dfa.DFARunner;
import com.kilic.kmeta.core.alls.stream.IStream;
import com.kilic.kmeta.core.alls.tn.IState.StateType;

/**
 * Algorithm from:
 * 
 * PARR T., HARWELL S., FISHER K. Adaptive LL(*) Parsing: The Power of Dynamic
 * Analysis
 */
public class ALLSParser {
	public void parse(ATN atn, IStream input) {
		ATNState p = atn.getStartState();
		RegularCallStack callStack = new RegularCallStack();
		ATNState oldp = p;

		while (true) {
			if (p == atn.getFinalState())
				return;
			// else pop stacks and update p

			if (p.hasNext()) {
				IATNEdge e = p.nextEdge();

				if (e instanceof ATNEpsilonEdge) {
					p = e.getTo();
				} else if (e instanceof ATNCallEdge) {
					ATNCallEdge ace = (ATNCallEdge) e;
					callStack.push(e.getTo());
					p = ace.getATN().getStartState();
				} else if (e instanceof ATNMutatorEdge) {
					ATNMutatorEdge ame = (ATNMutatorEdge) e;
					p = ame.getTo();
				} else if (e instanceof ATNPredicateEdge) {
					ATNPredicateEdge ape = (ATNPredicateEdge) e;
					// error if !ape.evaluatePredicate();
					p = ape.getTo();
				} else if (e instanceof ATNCharSetEdge) {
					ATNCharSetEdge cse = (ATNCharSetEdge) e;
					char c = input.nextChar();
					System.out.println("matched charset <" + e.getLabel() + "> : " + c);
					p = cse.getTo();
				} else if (e instanceof ATNStringEdge) {
					ATNStringEdge se = (ATNStringEdge) e;
					String str = input.nextString(se.getString().length());
					System.out.println("matched string <" + se.getLabel() + "> : " + str);
					p = se.getTo();
				} else if (e instanceof ATNTokenEdge) {
					ATNTokenEdge te = (ATNTokenEdge) e;
					DFARunner runner = new DFARunner(te.getTokenDFA());
					System.out.println("matched token <" + te.getLabel() + "> :" + runner.match(input));
					p = te.getTo();
				}
			} else if (p.isFinalState()) {
				if( callStack.isEmpty() ) {
					System.out.println("ERROR empty callstack");
					return;
				}
				p = callStack.pop();
			} else if (p.isDecisionState()) {
				BasicATNSimulator slas = new BasicATNSimulator(input);
				IATNEdge predictedEdge = slas.adaptivePredict(p, callStack);
				p = predictedEdge.getTo();
			} else {
				// error.
				return;
			}

			assert (p != oldp);
			oldp = p;
		}
	}
}
