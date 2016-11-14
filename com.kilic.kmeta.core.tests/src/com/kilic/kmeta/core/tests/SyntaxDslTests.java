package com.kilic.kmeta.core.tests;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.kilic.kmeta.core.alls.atn.ATN;
import com.kilic.kmeta.core.alls.parser.ALLSParser;
import com.kilic.kmeta.core.alls.stream.StringStream;
import com.kilic.kmeta.core.meta.Multiplicity;
import com.kilic.kmeta.core.syntax.ATNCallExpr;
import com.kilic.kmeta.core.syntax.AlternativeExpr;
import com.kilic.kmeta.core.syntax.AppendCurrentRetValToListMutator;
import com.kilic.kmeta.core.syntax.AssignCurrentRetValToFieldMutator;
import com.kilic.kmeta.core.syntax.AssignMatchStringToFieldMutator;
import com.kilic.kmeta.core.syntax.CharSetExpr;
import com.kilic.kmeta.core.syntax.CreateObjectMutator;
import com.kilic.kmeta.core.syntax.DelimiterExpr;
import com.kilic.kmeta.core.syntax.MultiplicityExpr;
import com.kilic.kmeta.core.syntax.MutatorExpr;
import com.kilic.kmeta.core.syntax.POJOParserContext;
import com.kilic.kmeta.core.syntax.ResetMatchStringMutator;
import com.kilic.kmeta.core.syntax.SequenceExpr;
import com.kilic.kmeta.core.syntax.StringExpr;
import com.kilic.kmeta.core.util.CharSet;

public class SyntaxDslTests {
	String desktopPath;

	ATN Grammar = new ATN();
	ATN Rule = new ATN();
	ATN E = new ATN();

	ATN AltE = new ATN();
	ATN SeqE = new ATN();
	ATN DelimE = new ATN();
	ATN MulE = new ATN();

	ATN PrimE = new ATN();

	ATN NotE = new ATN();
	ATN CharSetE = new ATN();
	ATN ParenE = new ATN();
	ATN StrL = new ATN();

	ATN CharRangeL = new ATN();
	ATN ID = new ATN();

	String grammar;

	@Before
	public void init() {
		desktopPath = System.getProperty("user.home") + "\\Desktop\\dot\\";

		// @formatter:off
		/** Grammar: (Rule|Enum)+;
			Rule: ID ':' Expr';';
			Enum: 'enum' ID ':' EnumL ( '|' EnumL )* ';';
			EnumL: ID '=' SeqE;
			Expr: AltE;
			AltE: SeqE | SeqE ('|' SeqE)+;
			SeqE: DelimE | DelimE ('_'? DelimE)+;
			DelimE: MulE | MulE ( '/' PrimE );
			MulE: PrimE | PrimE MulType;
			PrimE: NewInstE | FnCallE | AsgE |
			       ParenE | NotE | CharSetE | StrL | RuleRefL;
			
			NewInstE: '{' FQN ( '(' (Expr (',' Expr)*)? ')')? '}';
			FnCallE: FQN '(' ( PrimE (',' PrimE)? )* ')';
			AsgE: ID AsgType DelimE;
			
			ParenE: '(' Expr ')';
			NotE: '~' CharSetE;
			CharSetE: '[' (.|CharRangeL) (',' (.|CharRangeL))* ']';
			CharRangeL: . '-' .;
			StrL: '\'' ~'\''* '\'';
			RuleRefL: ID;
			
			AsgType: '=' | '+=' | '?=';
			MulType: '?' | '*' | '+';
			SpcType: '_';
			
			FQN : ID ('.' ID)*;
			
			ID	: [A-Za-z]+;
			WS	: [ \t\r\n]+;
		 */
		
		Utils.createATNFromSyntax(ID,
			new SequenceExpr(
				new StringExpr("$"),
				new MultiplicityExpr(Multiplicity.ONEORMORE, 
					new CharSetExpr(CharSet.LETTER)
				)
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
				new MutatorExpr(new CreateObjectMutator("CharSetE")),
				new StringExpr("["),
				new DelimiterExpr(
					new AlternativeExpr(
						new SequenceExpr(
							new MutatorExpr(new ResetMatchStringMutator()),
							new CharSetExpr(CharSet.ANY),
						),
						new ATNCallExpr(CharRangeL)
					), 
					",",
					Multiplicity.ONEORMORE
				),
				new StringExpr("]")
			)
		).setLabel("CharSetE");
			
		Utils.createATNFromSyntax(ParenE,
			new SequenceExpr(
				new MutatorExpr(new CreateObjectMutator("ParenE")),
				new StringExpr("("),
				new ATNCallExpr(E),
				new MutatorExpr(new AssignCurrentRetValToFieldMutator("expr")),
				new StringExpr(")")
			)
		).setLabel("ParenE");
		
		Utils.createATNFromSyntax(PrimE,
			new AlternativeExpr(
				new ATNCallExpr(ParenE),
				new ATNCallExpr(NotE),
				new ATNCallExpr(CharSetE),
				new ATNCallExpr(StrL),
				new ATNCallExpr(ID)
			)
		).setLabel("PrimE");
		
		Utils.createATNFromSyntax(MulE,
			new SequenceExpr(
				new ATNCallExpr(PrimE),
				new MultiplicityExpr(Multiplicity.OPTIONAL,
					new SequenceExpr(
						new MutatorExpr(new CreateObjectMutator("MulE")),
						new MutatorExpr(new AssignCurrentRetValToFieldMutator("expr")),
						new MutatorExpr(new ResetMatchStringMutator()),
						new CharSetExpr(new CharSet().addSingleton('?').addSingleton('+').addSingleton('*')),
						new MutatorExpr(new AssignMatchStringToFieldMutator("delimiter"))
					)
				)
			)
		).setLabel("MulE");
		
		Utils.createATNFromSyntax(DelimE,
			new SequenceExpr(
				new ATNCallExpr(MulE),
				new MultiplicityExpr(Multiplicity.OPTIONAL,
					new SequenceExpr(
						new MutatorExpr(new CreateObjectMutator("DelimE")),
						new MutatorExpr(new AssignCurrentRetValToFieldMutator("expr")),
						new CharSetExpr(new CharSet().addSingleton('/')),
						new MutatorExpr(new ResetMatchStringMutator()),
						new ATNCallExpr(StrL),
						new MutatorExpr(new AssignMatchStringToFieldMutator("delimiter"))
					)
				)
			)
		).setLabel("DelimE");

		Utils.createATNFromSyntax(SeqE,
			new SequenceExpr(
				new ATNCallExpr(DelimE),
				new MultiplicityExpr(Multiplicity.ANY,
					new SequenceExpr(
						new MutatorExpr(new CreateObjectMutator("SeqE")),
						new ATNCallExpr(DelimE),
						new MutatorExpr(new AppendCurrentRetValToListMutator("elems"))
					)
				)
			)
		).setLabel("SeqE");
		
		Utils.createATNFromSyntax(AltE, 
			new SequenceExpr(
				new ATNCallExpr(SeqE),
				new MultiplicityExpr(Multiplicity.ANY, 
					new SequenceExpr(
						new MutatorExpr(new CreateObjectMutator("AltE")),
						new MutatorExpr(new AppendCurrentRetValToListMutator("alts")),
						new StringExpr("|"), 
						new ATNCallExpr(SeqE),
						new MutatorExpr(new AppendCurrentRetValToListMutator("alts"))
					)
				)
			)
		).setLabel("AltE");
		
		Utils.createATNFromSyntax(E,
			new ATNCallExpr(AltE)
		).setLabel("E");
		
		Utils.createATNFromSyntax(Rule,
			new SequenceExpr(
				new MutatorExpr(new CreateObjectMutator("Rule")),
				new ATNCallExpr(ID),
				new MutatorExpr(new AssignMatchStringToFieldMutator("name")),
				new StringExpr(":"),
				new ATNCallExpr(E),
				new MutatorExpr(new AssignCurrentRetValToFieldMutator("expr")),
				new StringExpr(";")
			)
		).setLabel("Rule");
		
		Utils.createATNFromSyntax(Grammar,
			new MultiplicityExpr(Multiplicity.ONEORMORE,
				new SequenceExpr(
					new MutatorExpr(new CreateObjectMutator("Grammar")),
					new ATNCallExpr(Rule),
					new MutatorExpr(new AppendCurrentRetValToListMutator("rules"))
				)
			)
		).setLabel("Grammar"); 
	
		grammar = "$Grammar: $Rule+;" +
				  "$Rule: $ID ':' $E ';';" +
				  "$E: $AltE;" +
				  "$AltE: $DelimE+ ( '|' $DelimE+ )*;" +
				  "$DelimE: $MulE ('/' $StrL)?;" +
				  "$MulE: $PrimE [?,+,*]?;" +
				  "$PrimE: $ParenE | $NotE | $CharSetE | $StrL | $ID;" +
				  "$ParenE: '(' $E ')';" +
				  "$NotE: '~' $CharSetE;" +
				  "$CharSetE: '[' ([.] | $CharRangeL)+/',' ']';" +
				  "$StrL: ['] ~[']+ ['];" +
				  "$CharRangeL: [.] '-' [.];" +
				  "$ID: [$] [A-Z,a-z]+;";
		// @formatter:on
	}

	@Test
	public void atnTest() throws IOException {
		String gvFilePath = desktopPath + "Grammar.graphviz";
		Utils.dumpTNsTofile(gvFilePath, Grammar, Rule, E, AltE, DelimE, MulE, PrimE, ParenE, NotE, CharSetE, StrL,
				CharRangeL, ID);
		Utils.graphVizToSvg(gvFilePath);

		ALLSParser parser = new ALLSParser();

		String grammarWithNoSpaces = grammar.replace(" ", "");
		POJOParserContext pojoContext = new POJOParserContext();
		parser.parse(Grammar, new StringStream(grammarWithNoSpaces), pojoContext);
	}

	@Test
	public void syntaxDslPerformanceMeasurementTest() throws IOException {
		ALLSParser parser = new ALLSParser();
		String grammarWithNoSpaces = grammar.replace(" ", "");

		for (int s = 1; s <= 10001; s += 500) {
			StringBuilder input = new StringBuilder();
			for (int i = 0; i < s; i++) {
				input.append(grammarWithNoSpaces);
			}
			long start = System.nanoTime() / 1000000;
			parser.parse(Grammar, new StringStream(input.toString()), new POJOParserContext());
			long end = System.nanoTime() / 1000000;
			long dt = end - start;
			double avg = 1000.0 * (double) dt / (double) (s * input.length());
			System.out.println("s:" + s + " t:" + dt + " len:" + input.length() + " avg:" + avg + "ms/c");
		}
	}
}
