package com.kilic.kmeta.core.tests;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.kilic.kmeta.core.alls.atn.ATN;
import com.kilic.kmeta.core.alls.parser.ALLSParser;
import com.kilic.kmeta.core.alls.stream.IStream;
import com.kilic.kmeta.core.alls.stream.StringStream;
import com.kilic.kmeta.core.alls.syntax.ATNCallExpr;
import com.kilic.kmeta.core.alls.syntax.AlternativeExpr;
import com.kilic.kmeta.core.alls.syntax.CharSetExpr;
import com.kilic.kmeta.core.alls.syntax.MultiplicityExpr;
import com.kilic.kmeta.core.alls.syntax.SequenceExpr;
import com.kilic.kmeta.core.alls.syntax.StringExpr;
import com.kilic.kmeta.core.meta.Multiplicity;
import com.kilic.kmeta.core.util.CharSet;
import com.kilic.kmeta.core.util.ParseTreeDumper;

public class ATNTests {
	String desktopPath;

	ATN DecL = new ATN();
	ATN HexL = new ATN();
	ATN ParenE = new ATN();
	ATN PrimE = new ATN();

	ATN MulE = new ATN();
	ATN AddE = new ATN();
	ATN E = new ATN();
	ATN Body = new ATN();

	@Before
	public void init() {
		desktopPath = System.getProperty("user.home") + "\\Desktop\\dot\\";

		// @formatter:off
		Utils.createATNFromSyntax(DecL, 
			new MultiplicityExpr(Multiplicity.ONEORMORE, 
				new CharSetExpr(CharSet.DEC)
			)
		).setLabel("DecL");

		Utils.createATNFromSyntax(HexL, 
			new SequenceExpr(
				new MultiplicityExpr(Multiplicity.ONEORMORE, 
					new CharSetExpr(CharSet.HEX)
				),
				new StringExpr("h")
			)
		).setLabel("HexL");

		Utils.createATNFromSyntax(ParenE,
			new SequenceExpr(
				new StringExpr("("), 
				new ATNCallExpr(E), 
				new StringExpr(")")
			)
		).setLabel("ParenE");

		Utils.createATNFromSyntax(PrimE,
			new AlternativeExpr(
				new ATNCallExpr(DecL), 
				new ATNCallExpr(HexL), 
				new ATNCallExpr(ParenE)
			)
		).setLabel("PrimE");

		Utils.createATNFromSyntax(MulE, 
			new SequenceExpr(
				new ATNCallExpr(PrimE),
				new MultiplicityExpr(Multiplicity.ANY, 
					new SequenceExpr(
						new CharSetExpr(new CharSet().addSingleton('*').addSingleton('/')), 
						new ATNCallExpr(PrimE)
					)
				)
			)
		).setLabel("MulE");

		Utils.createATNFromSyntax(AddE, 
			new SequenceExpr(
				new ATNCallExpr(MulE),
				new MultiplicityExpr(Multiplicity.ANY, 
					new SequenceExpr(
							new CharSetExpr(new CharSet().addSingleton('+').addSingleton('-')), 
						new ATNCallExpr(MulE)
					)
				)
			)
		).setLabel("AddE");

		Utils.createATNFromSyntax(E, 
			new ATNCallExpr(AddE)
		).setLabel("E");

		Utils.createATNFromSyntax(Body,
			new MultiplicityExpr(Multiplicity.ANY, 
				new SequenceExpr(
					new ATNCallExpr(E), 
					new StringExpr(";")
				)
			)
		).setLabel("Body");
		
		// @formatter:on
	}

	@Test
	public void atnTest() throws IOException {
		String gvFilePath = desktopPath + "Body.graphviz";
		Utils.dumpTNsTofile(gvFilePath, Body, E, AddE, MulE, PrimE, ParenE, HexL, DecL);
		Utils.graphVizToSvg(gvFilePath);

		ALLSParser parser = new ALLSParser();
		parser.setListener(new ParseTreeDumper());

		IStream input = new StringStream("1+2*((3+(4+(5+6))))*7h+FADECAFEh+11;");
		parser.parse(Body, input);
	}

	@Test
	public void atnPerformanceTest() throws IOException {
		ALLSParser parser = new ALLSParser();
		StringBuilder inputString = new StringBuilder();

		for (int s = 50; s < 20000; s += 500) {
			inputString = new StringBuilder();
			for (int i = 0; i < s; i++)
				// inputString.append("1+2+3+4+5+6+7+8+9+0;");
				// int s = 1;
				inputString.append("1+2*((3+(4+(5+6))))*7h+FADECAFEh+11;");
			IStream input = new StringStream(inputString.toString());
			long start = System.nanoTime() / 1000000;
			parser.parse(Body, input);
			long end = System.nanoTime() / 1000000;
			System.out.println("s:" + s + " t:" + (end - start) + " size: " + inputString.length());
		}

	}
}
