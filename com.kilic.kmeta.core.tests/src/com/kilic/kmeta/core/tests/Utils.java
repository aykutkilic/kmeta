package com.kilic.kmeta.core.tests;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import com.kilic.kmeta.core.automaton.Automaton;
import com.kilic.kmeta.core.automaton.AutomatonState;
import com.kilic.kmeta.core.syntax.ISyntaxExpr;

public class Utils {
	public static Automaton createNFAFromSyntax(ISyntaxExpr e) {
		Automaton enfa = new Automaton();
		AutomatonState startState = enfa.createState();
		enfa.setStartState(startState);
		AutomatonState finalState = e.appendToNFA(enfa, startState, null);
		finalState.setFinal(true);

		return enfa;
	}
	
	public static void dumpAutomatonToFile(Automaton a, String filePath) throws FileNotFoundException {
		PrintWriter out = new PrintWriter(filePath);
		out.append(a.toGraphviz());
		out.close();
	}
}
