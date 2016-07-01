package com.kilic.kmeta.core.tests;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import com.kilic.kmeta.core.dfa.DFA;
import com.kilic.kmeta.core.dfa.DFAState;
import com.kilic.kmeta.core.syntax.ISyntaxExpr;

public class Utils {
	public static DFA createNFAFromSyntax(ISyntaxExpr e) {
		DFA enfa = new DFA();
		DFAState startState = enfa.createState();
		enfa.setStartState(startState);
		DFAState finalState = e.appendToNFA(enfa, startState, null);
		finalState.setFinal(true);

		return enfa;
	}
	
	public static void dumpAutomatonToFile(DFA a, String filePath) throws FileNotFoundException {
		PrintWriter out = new PrintWriter(filePath);
		out.append(a.toGraphviz());
		out.close();
	}
}
