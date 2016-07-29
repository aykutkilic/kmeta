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
	int indent = 0;

	public void parse(ATN atn, IStream input) {
		ATNState p = atn.getStartState();
		RegularCallStack callStack = new RegularCallStack();
		ATNState oldp = p;

		while (true) {
			if (p == atn.getFinalState())
				return;

			if (p.hasNext()) {
				IATNEdge e = p.nextEdge();

				// print("Applying " + p.toString() + " edge " +
				// e.toString());

				if (e instanceof ATNEpsilonEdge) {
					p = e.getTo();
				} else if (e instanceof ATNCallEdge) {
					ATNCallEdge ace = (ATNCallEdge) e;
					callStack.push(e.getTo());
					// print("Callstack: " + callStack);
					p = ace.getATN().getStartState();
					//print(ace.getATN().getLabel() + ":");
					indent++;
				} else if (e instanceof ATNMutatorEdge) {
					ATNMutatorEdge ame = (ATNMutatorEdge) e;
					p = ame.getTo();
				} else if (e instanceof ATNPredicateEdge) {
					ATNPredicateEdge ape = (ATNPredicateEdge) e;
					// error if !ape.evaluatePredicate();
					p = ape.getTo();
				} else if (e instanceof ATNCharSetEdge) {
					ATNCharSetEdge cse = (ATNCharSetEdge) e;
					/*char c =*/ input.nextChar();
					//print(String.valueOf(c));
					//print("Matched charset <" + e.getLabel() + "> : " + c);
					p = cse.getTo();
				}
				// print("New p=" + p);
			} else if (p.isFinalState()) {
				if (callStack.isEmpty()) {
					// print("ERROR empty callstack");
					return;
				}
				// print("Callstack: " + callStack);
				// print("Peek: " + callStack.peek());
				p = callStack.pop();
				indent--;
				// print("Returned to " + p);
			} else if (p.isDecisionState()) {
				// print("Making prediction " + p + " " +
				// input.lookAheadString(0, 5));
				BasicATNSimulator slas = new BasicATNSimulator(input);
				IATNEdge predictedEdge = slas.adaptivePredict(p, callStack);
				p = predictedEdge.getTo();
				// print("Predicted " + predictedEdge);
			} else {
				// error.
				System.out.println("ATN ERROR! " + p);
				return;
			}

			assert (p != null && p != oldp);
			oldp = p;
		}
	}

	void print(String input) {
		StringBuilder output = new StringBuilder();
		for (int i = 0; i < indent; i++)
			output.append("  ");
		output.append(input);
		System.out.println(output.toString());
	}
}
