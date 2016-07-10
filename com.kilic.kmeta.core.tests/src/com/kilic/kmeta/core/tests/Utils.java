package com.kilic.kmeta.core.tests;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import com.kilic.kmeta.core.alls.atn.ATN;
import com.kilic.kmeta.core.alls.syntax.ISyntaxExpr;
import com.kilic.kmeta.core.alls.tn.ITransitionNetwork;

public class Utils {
	public static ATN createATNFromSyntax(ATN atn, ISyntaxExpr e) {
		if(atn == null)
			atn = new ATN();
		
		e.appendToATN(atn, atn.getStartState(), atn.getFinalState());
		return atn;
	}
	
	public static void dumpTNToFile(ITransitionNetwork<?,?> tn, String filePath) throws FileNotFoundException {
		PrintWriter out = new PrintWriter(filePath);
		out.append(tn.toGraphviz());
		out.close();
	}
	
}
