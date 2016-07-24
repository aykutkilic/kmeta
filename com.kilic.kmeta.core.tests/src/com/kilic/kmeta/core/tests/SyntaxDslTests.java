package com.kilic.kmeta.core.tests;

import java.io.FileNotFoundException;

import org.junit.Before;
import org.junit.Test;

import com.kilic.kmeta.core.alls.atn.ATN;
import com.kilic.kmeta.core.alls.syntax.ATNCallExpr;
import com.kilic.kmeta.core.alls.syntax.AlternativeExpr;
import com.kilic.kmeta.core.alls.syntax.CharSetExpr;
import com.kilic.kmeta.core.alls.syntax.MultiplicityExpr;
import com.kilic.kmeta.core.alls.syntax.SeparatorExpr;
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

	ATN NotE = new ATN();
	ATN CharSetE = new ATN();
	ATN ParenE = new ATN();
	ATN StrL = new ATN();
	ATN RuleRefL = new ATN();

	ATN CharRangeL = new ATN();
	ATN ID = new ATN();
	
	String grammar;
	
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
		
		Utils.createATNFromSyntax(NotE, 
			new SequenceExpr(
				new StringExpr("~"),
				new ATNCallExpr(CharSetE)
			)
		).setLabel("NotE");
		
		Utils.createATNFromSyntax(CharSetE, 
			new SequenceExpr(
				new StringExpr("["),
				new SeparatorExpr(
					new AlternativeExpr(
						new CharSetExpr(CharSet.ANY),
						new ATNCallExpr(CharRangeL)
					), 
					","
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
				new ATNCallExpr(NotE),
				new ATNCallExpr(CharSetE),
				new ATNCallExpr(StrL),
				new ATNCallExpr(RuleRefL)
			)
		).setLabel("PrimE");
		
		Utils.createATNFromSyntax(MulE,
			new SequenceExpr(
				new ATNCallExpr(PrimE),
				new MultiplicityExpr(Multiplicity.OPTIONAL,
					new CharSetExpr(new CharSet().addSingleton('+').addSingleton('*'))
				)
			)
		).setLabel("MulE");
		
		Utils.createATNFromSyntax(AltE, 
			new SequenceExpr(
				new MultiplicityExpr(Multiplicity.ONEORMORE,
					new ATNCallExpr(MulE)
				),
				new MultiplicityExpr(Multiplicity.ANY, 
					new SequenceExpr(
						new StringExpr("|"), 
						new MultiplicityExpr(Multiplicity.ONEORMORE,
							new ATNCallExpr(MulE)
						)
					)
				)
			)
		).setLabel("AltE");
		
		Utils.createATNFromSyntax(E,
			new ATNCallExpr(AltE)
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
	
		grammar = "Grammar: Rule+;" +
				  "Rule: ID ':' E ';';" +
				  "E: AltE;" +
				  "AltE: MulE+ ( '|' MulE+ )*;" +
				  "MulE: PrimE [+,*]?;" +
				  "PrimE: ParenE | NotE | CharSetE | StrL | RuleRefL;" +
				  "ParenE: '(' E ')';"+
				  "CharSetE: '[' (. | CharRangeL)+\',' ']';"+
				  "RuleRefL: ID;"+
				  "StrL: ['] ~[']+ [']"+
				  "CharRangeL: . '-' .;"+
				  "ID: LETTER+;";
		// @formatter:on
	}

	@Test
	public void atnTest() throws FileNotFoundException {
		ID.reduceToTokenDFAEdge();
		CharRangeL.reduceToTokenDFAEdge();

		Utils.dumpTNsTofile(desktopPath + "Grammar.graphviz", Grammar, Rule, E, MulE, AltE, PrimE, NotE, CharSetE, ParenE,
				StrL, RuleRefL);
	}

}
