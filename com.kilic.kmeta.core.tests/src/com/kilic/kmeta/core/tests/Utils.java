package com.kilic.kmeta.core.tests;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;

import com.kilic.kmeta.core.alls.atn.ATN;
import com.kilic.kmeta.core.alls.tn.ITransitionNetwork;
import com.kilic.kmeta.core.alls.tn.TNUtils;
import com.kilic.kmeta.core.alls.tn.TransitionNetworkBase;
import com.kilic.kmeta.core.syntax.ISyntaxExpr;

public class Utils {
	public static ATN createATNFromSyntax(ATN atn, ISyntaxExpr e) {
		if (atn == null)
			atn = new ATN();

		e.appendToATN(atn, atn.getStartState(), atn.getFinalState());
		return atn;
	}

	public static void dumpTNToFile(ITransitionNetwork<?, ?> tn, String filePath) throws FileNotFoundException {
		PrintWriter out = new PrintWriter(filePath);
		out.append(tn.toGraphviz());
		out.close();
	}

	public static void dumpTNsTofile(String filePath, TransitionNetworkBase<?, ?, ?>... tns)
			throws FileNotFoundException {
		PrintWriter out = new PrintWriter(filePath);

		Collection<TransitionNetworkBase<?, ?, ?>> tnsList = new ArrayList<>();
		for (TransitionNetworkBase<?, ?, ?> tn : tns)
			tnsList.add(tn);

		out.append(TNUtils.toGraphviz(tnsList));
		out.close();
	}

	public static void graphVizToSvg(String gvFilePath) throws IOException {
		String svgFilePath;
		int i = gvFilePath.lastIndexOf('.');
		if (i == -1)
			svgFilePath = gvFilePath + ".svg";
		else
			svgFilePath = gvFilePath.substring(0, i) + ".svg";

		Runtime.getRuntime().exec("dot -Tsvg " + gvFilePath + " -o " + svgFilePath);
	}

}
