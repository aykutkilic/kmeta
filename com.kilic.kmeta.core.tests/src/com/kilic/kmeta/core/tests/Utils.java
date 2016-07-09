package com.kilic.kmeta.core.tests;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import com.kilic.kmeta.core.atn.ATN;
import com.kilic.kmeta.core.predictiondfa.PredictionDFA;
import com.kilic.kmeta.core.syntax.ISyntaxExpr;

public class Utils {
	public static ATN createATNFromSyntax(ATN atn, ISyntaxExpr e) {
		if(atn == null)
			atn = new ATN();
		
		e.appendToATN(atn, atn.getStartState(), atn.getFinalState());
		return atn;
	}
	
	public static void dumpATNToFile(ATN a, String filePath) throws FileNotFoundException {
		PrintWriter out = new PrintWriter(filePath);
		out.append(a.toGraphviz());
		out.close();
	}
	
	public static void dumpAutomatonToFile(PredictionDFA a, String filePath) throws FileNotFoundException {
		PrintWriter out = new PrintWriter(filePath);
		out.append(a.toGraphviz());
		out.close();
	}
}
