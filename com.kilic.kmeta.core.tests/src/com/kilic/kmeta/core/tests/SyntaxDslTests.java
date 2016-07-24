package com.kilic.kmeta.core.tests;

import java.io.FileNotFoundException;

import org.junit.Before;
import org.junit.Test;

import com.kilic.kmeta.core.alls.atn.ATN;
import com.kilic.kmeta.core.alls.syntax.ATNCallExpr;
import com.kilic.kmeta.core.alls.syntax.AlternativeExpr;
import com.kilic.kmeta.core.alls.syntax.CharSetExpr;
import com.kilic.kmeta.core.alls.syntax.MultiplicityExpr;
import com.kilic.kmeta.core.alls.syntax.SequenceExpr;
import com.kilic.kmeta.core.alls.syntax.StringExpr;
import com.kilic.kmeta.core.meta.Multiplicity;
import com.kilic.kmeta.core.util.CharSet;

public class SyntaxDslTests {
	String desktopPath;
	
	ATN Grammar = new ATN();
	ATN Rule = new ATN();
	ATN E = new ATN();
	ATN MulE = new ATN();
	ATN AltE = new ATN();
	
	ATN PrimE = new ATN();
	
	ATN CharSetE = new ATN();
	ATN ParenE = new ATN();
	ATN StrL = new ATN();
	ATN RuleRefL = new ATN();
	
	ATN CharRangeL = new ATN();
	ATN ID = new ATN();

	@Before
	public void init() {
		desktopPath = System.getProperty("user.home") + "\\Desktop\\dot\\";

		// @formatter:off
		Utils.createATNFromSyntax(ID, 
			new MultiplicityExpr(Multiplicity.ONEORMORE, 
				new CharSetExpr(CharSet.LETTER)
			)
		).setLabel("ID");

		Utils.createATNFromSyntax(StrL, 
			new SequenceExpr(
				new StringExpr("'"),
				new MultiplicityExpr(Multiplicity.ONEORMORE, 
					new CharSetExpr(CharSet.ANY.getSubtraction(new CharSet().addSingleton('\'')))
				),
				new StringExpr("'")
			)
		).setLabel("StrL");
		
		Utils.createATNFromSyntax(RuleRefL, 
			new ATNCallExpr(ID)
		).setLabel("RuleRefL");
	
		Utils.createATNFromSyntax(CharRangeL, 
			new SequenceExpr(
				new CharSetExpr(CharSet.ANY),
				new StringExpr("-"),
				new CharSetExpr(CharSet.ANY)
			)
		).setLabel("CharRangeL");
		
		Utils.createATNFromSyntax(CharSetE, 
			new SequenceExpr(
				new StringExpr("["),
				new MultiplicityExpr(Multiplicity.ONEORMORE, 
					new ATNCallExpr(CharRangeL)
				),
				new StringExpr("]")
			)
		).setLabel("CharSetE");
			
		Utils.createATNFromSyntax(ParenE,
			new SequenceExpr(
				new StringExpr("("), 
				new ATNCallExpr(E),
				new StringExpr(")")
			)
		).setLabel("ParenE");
		
		Utils.createATNFromSyntax(PrimE,
			new AlternativeExpr(
				new ATNCallExpr(ParenE),
				new ATNCallExpr(CharSetE),
				new ATNCallExpr(StrL),
				new ATNCallExpr(RuleRefL)
			)
		).setLabel("PrimE");
		
		Utils.createATNFromSyntax(AltE, 
			new SequenceExpr(
				new ATNCallExpr(PrimE),
				new MultiplicityExpr(Multiplicity.ANY, 
					new SequenceExpr(
						new StringExpr("|"), 
						new ATNCallExpr(E)
					)
				)
			)
		).setLabel("AltE");
		
		Utils.createATNFromSyntax(MulE,
			new SequenceExpr(
				new ATNCallExpr(AltE),
				new MultiplicityExpr(Multiplicity.OPTIONAL,
					new CharSetExpr(new CharSet().addSingleton('+').addSingleton('*'))
				)
			)
		).setLabel("MulE");
		
		Utils.createATNFromSyntax(E,
			new ATNCallExpr(MulE)
		).setLabel("E");
		
		Utils.createATNFromSyntax(Rule,
			new SequenceExpr(
				new ATNCallExpr(ID),
				new StringExpr(":"),
				new ATNCallExpr(E),
				new StringExpr(";")
			)
		).setLabel("Rule");
		
		Utils.createATNFromSyntax(Grammar,
			new MultiplicityExpr(Multiplicity.ONEORMORE,
				new ATNCallExpr(Rule)
			)
		).setLabel("Grammar");
		
		// @formatter:on
	}
	
	
	@Test
	public void atnTest() throws FileNotFoundException {
		ID.reduceToTokenDFAEdge();
		CharRangeL.reduceToTokenDFAEdge();

		Utils.dumpTNsTofile(desktopPath + "Grammar.graphviz", Grammar, Rule, E, MulE, AltE, PrimE, CharSetE, ParenE, StrL, RuleRefL);
	}

}
