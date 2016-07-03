package com.kilic.kmeta.core.tests;

import java.io.FileNotFoundException;

import org.junit.Before;
import org.junit.Test;

import com.kilic.kmeta.core.atn.ATN;
import com.kilic.kmeta.core.meta.Multiplicity;
import com.kilic.kmeta.core.syntax.ATNCallExpr;
import com.kilic.kmeta.core.syntax.AlternativeExpr;
import com.kilic.kmeta.core.syntax.CharSetExpr;
import com.kilic.kmeta.core.syntax.MultiplicityExpr;
import com.kilic.kmeta.core.syntax.SequenceExpr;
import com.kilic.kmeta.core.syntax.StringExpr;
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
	
	@Before
	public void init() {
		desktopPath = System.getProperty("user.home") + "\\Desktop\\";
		
		// @formatter:off
		Utils.createATNFromSyntax( 
			DecL,
			new MultiplicityExpr(
				Multiplicity.ONEORMORE, 
				new CharSetExpr(CharSet.DEC)
			)
		).setLabel("DecL");
		
		Utils.createATNFromSyntax( 
			HexL,
			new SequenceExpr(
				new StringExpr("0x"),
				new MultiplicityExpr(
					Multiplicity.ONEORMORE,
					new CharSetExpr(CharSet.HEX)
				)
			)
		).setLabel("HexL");
		
		Utils.createATNFromSyntax(
			ParenE,
			new SequenceExpr(
				new StringExpr("("),
				new ATNCallExpr(E),
				new StringExpr(")")
			)
		).setLabel("ParenE");
		
		Utils.createATNFromSyntax(
			PrimE,
			new AlternativeExpr(
				new ATNCallExpr(DecL),
				new ATNCallExpr(HexL),
				new ATNCallExpr(ParenE)
			)
		).setLabel("PrimE");
		
		Utils.createATNFromSyntax(
			MulE,
			new SequenceExpr(
				new ATNCallExpr(PrimE),
				new StringExpr("*"),
				new ATNCallExpr(MulE)
			)
		).setLabel("MulE");
		
		Utils.createATNFromSyntax(
			AddE,
			new SequenceExpr(
				new ATNCallExpr(MulE),
				new StringExpr("+"),
				new ATNCallExpr(AddE)
			)
		).setLabel("AddE");
		
		Utils.createATNFromSyntax(
			E,
			new MultiplicityExpr(
				Multiplicity.ANY,
				new SequenceExpr(
					new ATNCallExpr(AddE),
					new StringExpr(";")
				)
			)
		).setLabel("E");
		// @formatter:on
	}
	
	@Test
	public void atnTest() throws FileNotFoundException {
		Utils.dumpATNToFile(E, desktopPath + "E.graphviz");
		Utils.dumpATNToFile(AddE, desktopPath + "AddE.graphviz");
		Utils.dumpATNToFile(MulE, desktopPath + "MulE.graphviz");
		Utils.dumpATNToFile(ParenE, desktopPath + "ParenE.graphviz");
		Utils.dumpATNToFile(HexL, desktopPath + "HexL.graphviz");
		Utils.dumpATNToFile(DecL, desktopPath + "DecL.graphviz");
	}
}
