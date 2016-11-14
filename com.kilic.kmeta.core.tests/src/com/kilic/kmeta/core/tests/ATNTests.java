package com.kilic.kmeta.core.tests;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.kilic.kmeta.core.alls.atn.ATN;
import com.kilic.kmeta.core.alls.parser.ALLSParser;
import com.kilic.kmeta.core.alls.stream.IStream;
import com.kilic.kmeta.core.alls.stream.StringStream;
import com.kilic.kmeta.core.meta.Multiplicity;
import com.kilic.kmeta.core.syntax.ATNCallExpr;
import com.kilic.kmeta.core.syntax.AlternativeExpr;
import com.kilic.kmeta.core.syntax.AppendCurrentRetValToListMutator;
import com.kilic.kmeta.core.syntax.AssignCurrentRetValToFieldMutator;
import com.kilic.kmeta.core.syntax.AssignMatchStringToFieldMutator;
import com.kilic.kmeta.core.syntax.CharSetExpr;
import com.kilic.kmeta.core.syntax.CreateObjectMutator;
import com.kilic.kmeta.core.syntax.MultiplicityExpr;
import com.kilic.kmeta.core.syntax.MutatorExpr;
import com.kilic.kmeta.core.syntax.POJOParserContext;
import com.kilic.kmeta.core.syntax.ResetMatchStringMutator;
import com.kilic.kmeta.core.syntax.SequenceExpr;
import com.kilic.kmeta.core.syntax.StringExpr;
import com.kilic.kmeta.core.tests.expr.Body;
import com.kilic.kmeta.core.util.CharSet;

public class ATNTests {
	private String desktopPath;

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
			new SequenceExpr(
				new MutatorExpr(new CreateObjectMutator("DecL")),
				new MultiplicityExpr(Multiplicity.ONEORMORE, 
					new CharSetExpr(CharSet.DEC)
				),
				new MutatorExpr(new AssignMatchStringToFieldMutator("val"))
			)
		).setLabel("DecL");

		Utils.createATNFromSyntax(HexL, 
			new SequenceExpr(
				new MutatorExpr(new CreateObjectMutator("HexL")),
				new MultiplicityExpr(Multiplicity.ONEORMORE, 
					new CharSetExpr(CharSet.HEX)
				),
				new StringExpr("h"),
				new MutatorExpr(new AssignMatchStringToFieldMutator("val"))
			)
		).setLabel("HexL");

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
				new ATNCallExpr(DecL), 
				new ATNCallExpr(HexL),
				new ATNCallExpr(ParenE)
			)
		).setLabel("PrimE");

		Utils.createATNFromSyntax(MulE, 
			new SequenceExpr(
				new ATNCallExpr(PrimE),
				new MultiplicityExpr(Multiplicity.OPTIONAL, 
					new SequenceExpr(
						new MutatorExpr(new CreateObjectMutator("MulE")),
						new MutatorExpr(new AssignCurrentRetValToFieldMutator("left")),
						new MutatorExpr(new ResetMatchStringMutator()),
						new CharSetExpr(new CharSet().addSingleton('*').addSingleton('/')),
						new MutatorExpr(new AssignMatchStringToFieldMutator("op")),
						new ATNCallExpr(MulE),
						new MutatorExpr(new AssignCurrentRetValToFieldMutator("right"))
					)
				)
			)
		).setLabel("MulE");

		Utils.createATNFromSyntax(AddE, 
			new SequenceExpr(
				new ATNCallExpr(MulE),
				new MultiplicityExpr(Multiplicity.OPTIONAL, 
					new SequenceExpr(
						new MutatorExpr(new CreateObjectMutator("AddE")),
						new MutatorExpr(new AssignCurrentRetValToFieldMutator("left")),
						new MutatorExpr(new ResetMatchStringMutator()),
						new CharSetExpr(new CharSet().addSingleton('+').addSingleton('-')),
						new MutatorExpr(new AssignMatchStringToFieldMutator("op")),
						new ATNCallExpr(AddE),
						new MutatorExpr(new AssignCurrentRetValToFieldMutator("right"))
					)
				)
			)
		).setLabel("AddE");

		Utils.createATNFromSyntax(E, 
			new ATNCallExpr(AddE)
		).setLabel("E");

		Utils.createATNFromSyntax(Body,
			new SequenceExpr(
				new MutatorExpr(new CreateObjectMutator("Body")),
				new MultiplicityExpr(Multiplicity.ANY, 
					new SequenceExpr(
						new ATNCallExpr(E),
						new MutatorExpr(new AppendCurrentRetValToListMutator("exprs")),
						new StringExpr(";")
					)
				)
			)
		).setLabel("Body");

		/**
		 * Body: {Body} (exprs+=E [;])*;
		 * E: AddE;
		 * AddE: MulE  ( {AddE} left=current op=[+,-] right=AddE )?;
		 * MulE: PrimE ( {MulE} left=current op=[*,/] right=MulE )?;
		 * PrimE: DecL | HexL | ParenE;
		 * ParenE: [(] {ParenE} expr=E [)];
		 * HexL: [0-9a-fA-F]+ [h] {HexL};
		 * DecL: [0-9]+ {DecL};
		 */
		
		// @formatter:on
	}

	@Test
	public void atnTest() throws IOException {
		String gvFilePath = desktopPath + "Body.graphviz";
		Utils.dumpTNsTofile(gvFilePath, Body, E, AddE, MulE, PrimE, ParenE, HexL, DecL);
		Utils.graphVizToSvg(gvFilePath);

		ALLSParser parser = new ALLSParser();

		IStream input = new StringStream("(1+2)*7h;1;1-1;FFh;10h*4;1+2*((3+(4+(5+6))))*7h+CAFEh-11;");
		POJOParserContext ctx = new POJOParserContext();
		ctx.addJavaPackage("com.kilic.kmeta.core.tests.expr");
		parser.parse(Body, input, ctx);

		Body body = (Body) ctx.getLocalObject();
		Assert.assertEquals((1 + 2) * 0x7, body.exprs.get(0).eval());
		Assert.assertEquals(1, body.exprs.get(1).eval());
		Assert.assertEquals(1 - 1, body.exprs.get(2).eval());
		Assert.assertEquals(0xFF, body.exprs.get(3).eval());
		Assert.assertEquals(0x10 * 4, body.exprs.get(4).eval());
		Assert.assertEquals(1 + 2 * ((3 + (4 + (5 + 6)))) * 0x7 + 0xCAFE - 11, body.exprs.get(5).eval());
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
			POJOParserContext ctx = new POJOParserContext();
			ctx.addJavaPackage("com.kilic.kmeta.core.tests.expr");
			parser.parse(Body, input, ctx);
			long end = System.nanoTime() / 1000000;
			System.out.println("s:" + s + " t:" + (end - start) + " size: " + inputString.length());
		}
	}
}
