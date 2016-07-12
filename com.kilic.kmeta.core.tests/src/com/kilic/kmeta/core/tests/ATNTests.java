package com.kilic.kmeta.core.tests;

import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import com.kilic.kmeta.core.alls.atn.ATN;
import com.kilic.kmeta.core.alls.nfa.NFA;
import com.kilic.kmeta.core.alls.parser.ALLSParser;
import com.kilic.kmeta.core.alls.stream.IStream;
import com.kilic.kmeta.core.alls.stream.StringStream;
import com.kilic.kmeta.core.alls.syntax.ATNCallExpr;
import com.kilic.kmeta.core.alls.syntax.AlternativeExpr;
import com.kilic.kmeta.core.alls.syntax.CharSetExpr;
import com.kilic.kmeta.core.alls.syntax.MultiplicityExpr;
import com.kilic.kmeta.core.alls.syntax.SequenceExpr;
import com.kilic.kmeta.core.alls.syntax.StringExpr;
import com.kilic.kmeta.core.alls.tn.TNUtils;
import com.kilic.kmeta.core.alls.tn.TransitionNetworkBase;
import com.kilic.kmeta.core.alls.tokendfa.TokenDFA;
import com.kilic.kmeta.core.meta.Multiplicity;
import com.kilic.kmeta.core.util.CharSet;

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
		Utils.createATNFromSyntax(DecL, new MultiplicityExpr(Multiplicity.ONEORMORE, new CharSetExpr(CharSet.DEC)))
				.setLabel("DecL");

		Utils.createATNFromSyntax(HexL, new SequenceExpr(new StringExpr("0x"),
				new MultiplicityExpr(Multiplicity.ONEORMORE, new CharSetExpr(CharSet.HEX)))).setLabel("HexL");

		Utils.createATNFromSyntax(ParenE,
				new SequenceExpr(new StringExpr("("), new ATNCallExpr(E), new StringExpr(")"))).setLabel("ParenE");

		Utils.createATNFromSyntax(PrimE,
				new AlternativeExpr(new ATNCallExpr(DecL), new ATNCallExpr(HexL), new ATNCallExpr(ParenE)))
				.setLabel("PrimE");

		Utils.createATNFromSyntax(MulE, new SequenceExpr(new ATNCallExpr(PrimE),
				new MultiplicityExpr(Multiplicity.ANY, new SequenceExpr(new StringExpr("*"), new ATNCallExpr(MulE)))))
				.setLabel("MulE");

		Utils.createATNFromSyntax(AddE, new SequenceExpr(new ATNCallExpr(MulE),
				new MultiplicityExpr(Multiplicity.ANY, new SequenceExpr(new StringExpr("+"), new ATNCallExpr(AddE)))))
				.setLabel("AddE");

		Utils.createATNFromSyntax(E, new ATNCallExpr(AddE)).setLabel("E");
		
		Utils.createATNFromSyntax(Body,
				new MultiplicityExpr(Multiplicity.ANY, new SequenceExpr(new ATNCallExpr(E), new StringExpr(";"))))
				.setLabel("Body");
		// @formatter:on
	}

	@Test
	public void atnToNFATest() throws FileNotFoundException {
		assertTrue(HexL.hasEquivalentNFA());
		NFA HexLNFA = HexL.getEquivalentNFA();
		Utils.dumpTNToFile(HexLNFA, desktopPath + "HexLNFA.graphviz");
		TokenDFA HexLDFA = HexLNFA.getEquivalentDFA();
		Utils.dumpTNToFile(HexLDFA, desktopPath + "HexLDFA.graphviz");
	}

	@Test
	public void dfaReductionTest() throws FileNotFoundException {
		HexL.reduceToTokenDFAEdge();
		DecL.reduceToTokenDFAEdge();
		Utils.dumpTNToFile(PrimE, desktopPath + "Prim_after_reduction.graphviz");
	}

	@Test
	public void atnTest() throws FileNotFoundException {
		HexL.reduceToTokenDFAEdge();
		DecL.reduceToTokenDFAEdge();

		Utils.dumpTNsTofile(desktopPath + "Body.graphviz", Body, E, AddE, MulE, PrimE, ParenE);

		ALLSParser parser = new ALLSParser();
		IStream input = new StringStream("1+2*3*(4+5)");
		parser.parse(Body, input);
	}
}
