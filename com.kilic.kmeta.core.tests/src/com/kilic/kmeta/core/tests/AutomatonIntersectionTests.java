package com.kilic.kmeta.core.tests;

import java.io.FileNotFoundException;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

import com.kilic.kmeta.core.alls.analysis.DiscriminationAutomatonComputer;
import com.kilic.kmeta.core.alls.analysis.IntersectionComputer;
import com.kilic.kmeta.core.alls.predictiondfa.CharSetMatcher;
import com.kilic.kmeta.core.alls.predictiondfa.PredictionDFA;
import com.kilic.kmeta.core.alls.predictiondfa.PredictionDFAState;
import com.kilic.kmeta.core.alls.predictiondfa.StringMatcher;
import com.kilic.kmeta.core.alls.syntax.AlternativeExpr;
import com.kilic.kmeta.core.alls.syntax.CharSetExpr;
import com.kilic.kmeta.core.alls.syntax.ISyntaxExpr;
import com.kilic.kmeta.core.alls.syntax.MultiplicityExpr;
import com.kilic.kmeta.core.alls.syntax.SequenceExpr;
import com.kilic.kmeta.core.alls.syntax.StringExpr;
import com.kilic.kmeta.core.meta.Multiplicity;
import com.kilic.kmeta.core.util.CharSet;

import static org.junit.Assert.assertEquals;

public class AutomatonIntersectionTests {
	PredictionDFA DecL;
	PredictionDFA HexL;
	PredictionDFA RealL;
	PredictionDFA IncrE;
	PredictionDFA AddE;
	PredictionDFA MulE;
	PredictionDFA ParenE;
	PredictionDFA NegE;
	PredictionDFA TerroristE;

	String desktopPath;

	@Before
	public void init() {
		desktopPath = System.getProperty("user.home") + "\\Desktop\\";

		// @formatter:off
		DecL = Utils.createNFAFromSyntax(
			new MultiplicityExpr(Multiplicity.ONEORMORE, 
				new CharSetExpr(CharSet.DEC)
			)
		).convertNFAToDFA();
		DecL.setLabel("DecL");
		
		HexL = Utils.createNFAFromSyntax(
			new SequenceExpr(
				new StringExpr("0x"),
				new MultiplicityExpr(Multiplicity.ONEORMORE, 
					new CharSetExpr(CharSet.HEX)
				)
			)
		).convertNFAToDFA();
		HexL.setLabel("HexL");
		
		TerroristE = Utils.createNFAFromSyntax(
			new StringExpr("0xAAA*1+2+3+6+5+-4e-34*5+++52*5")
		).convertNFAToDFA();
		TerroristE.setLabel("TerroristE");
		// @formatter:on

		createRealL();
		createNegE();
		createIncrE();
		createMulE();
		createAddE();
		createParenE();
	}

	private void createRealL() {
		CharSet eE = new CharSet();
		eE.addSingleton('e');
		eE.addSingleton('E');
		CharSet pM = new CharSet();
		pM.addSingleton('+');
		pM.addSingleton('-');

		// @formatter:off
		ISyntaxExpr E = new SequenceExpr(
			new CharSetExpr(eE),
			new MultiplicityExpr(Multiplicity.OPTIONAL, 
				new CharSetExpr(pM)),
			new MultiplicityExpr(Multiplicity.ONEORMORE, 
				new CharSetExpr(CharSet.DEC))
		);

		ISyntaxExpr RealLSyn = new AlternativeExpr(
			new SequenceExpr(
				new MultiplicityExpr(Multiplicity.ANY, 
					new CharSetExpr(CharSet.DEC)),
				new StringExpr("."), 
				new MultiplicityExpr(Multiplicity.ONEORMORE, 
					new CharSetExpr(CharSet.DEC)),
				new MultiplicityExpr(Multiplicity.OPTIONAL, 
					E)
			),

			new SequenceExpr(
				new MultiplicityExpr(Multiplicity.ONEORMORE, 
					new CharSetExpr(CharSet.DEC)), 
				E 
			)
		);

		// @formatter:on

		PredictionDFA RealLNFA = Utils.createNFAFromSyntax(RealLSyn);

		try {
			Utils.dumpAutomatonToFile(RealLNFA, desktopPath + "RealLNFA.graphviz");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		RealL = RealLNFA.convertNFAToDFA();
		RealL.setLabel("RealL");
	}

	private void createNegE() {
		NegE = new PredictionDFA();
		NegE.setLabel("NegE");

		PredictionDFAState startState = NegE.createState();
		PredictionDFAState midState = NegE.createState();
		PredictionDFAState finalState = NegE.createState();

		NegE.setStartState(startState);
		finalState.setFinal(true);

		NegE.createMatcherTransition(startState, midState, new CharSetMatcher(new CharSet().addSingleton('+', '-')));
		NegE.createCallTransition(midState, finalState, HexL);
		NegE.createCallTransition(midState, finalState, DecL);
		NegE.createCallTransition(midState, finalState, RealL);
	}

	private void createIncrE() {
		IncrE = new PredictionDFA();
		IncrE.setLabel("IncrE");

		PredictionDFAState startState = IncrE.createState();
		PredictionDFAState midState = IncrE.createState();
		PredictionDFAState finalState = IncrE.createState();

		IncrE.setStartState(startState);
		finalState.setFinal(true);

		IncrE.createCallTransition(startState, midState, NegE);
		IncrE.createCallTransition(startState, midState, DecL);
		IncrE.createCallTransition(startState, midState, HexL);
		IncrE.createCallTransition(startState, midState, RealL);
		IncrE.createMatcherTransition(midState, finalState, new StringMatcher("++"));
		IncrE.createMatcherTransition(midState, finalState, new StringMatcher("--"));
	}

	private void createAddE() {
		AddE = new PredictionDFA();

		PredictionDFAState s0 = AddE.createState();
		PredictionDFAState s1 = AddE.createState();
		PredictionDFAState s2 = AddE.createState();
		PredictionDFAState s3 = AddE.createState();

		AddE.setStartState(s0);
		s3.setFinal(true);

		AddE.createCallTransition(s0, s1, MulE);
		AddE.createCallTransition(s0, s1, NegE);
		AddE.createCallTransition(s0, s1, DecL);
		AddE.createCallTransition(s0, s1, HexL);
		AddE.createCallTransition(s0, s1, RealL);
		AddE.createCallTransition(s0, s1, IncrE);
		AddE.createMatcherTransition(s1, s2, new CharSetMatcher(new CharSet().addSingleton('+', '-')));
		AddE.createCallTransition(s0, s1, MulE);
		AddE.createCallTransition(s2, s3, NegE);
		AddE.createCallTransition(s2, s3, DecL);
		AddE.createCallTransition(s2, s3, HexL);
		AddE.createCallTransition(s2, s3, RealL);
		AddE.createCallTransition(s2, s3, IncrE);
		AddE.createEpsilonTransition(s3, s1);

		AddE = AddE.convertNFAToDFA();
		AddE.setLabel("AddE");
	}

	private void createMulE() {
		MulE = new PredictionDFA();

		PredictionDFAState s0 = MulE.createState();
		PredictionDFAState s1 = MulE.createState();
		PredictionDFAState s2 = MulE.createState();
		PredictionDFAState s3 = MulE.createState();

		MulE.setStartState(s0);
		s3.setFinal(true);

		MulE.createCallTransition(s0, s1, NegE);
		MulE.createCallTransition(s0, s1, IncrE);
		MulE.createCallTransition(s0, s1, DecL);
		MulE.createCallTransition(s0, s1, HexL);
		MulE.createCallTransition(s0, s1, RealL);
		MulE.createMatcherTransition(s1, s2, new CharSetMatcher(new CharSet().addSingleton('*', '/')));
		MulE.createCallTransition(s2, s3, NegE);
		MulE.createCallTransition(s2, s3, DecL);
		MulE.createCallTransition(s2, s3, HexL);
		MulE.createCallTransition(s2, s3, RealL);
		MulE.createCallTransition(s2, s3, IncrE);
		MulE.createEpsilonTransition(s3, s1);

		MulE = MulE.convertNFAToDFA();
		MulE.setLabel("MulE");
	}

	private void createParenE() {
		ParenE = new PredictionDFA();

		PredictionDFAState s0 = ParenE.createState();
		PredictionDFAState s1 = ParenE.createState();
		PredictionDFAState s2 = ParenE.createState();
		PredictionDFAState s3 = ParenE.createState();

		ParenE.setStartState(s0);
		s3.setFinal(true);

		ParenE.createMatcherTransition(s0, s1, new CharSetMatcher(new CharSet().addSingleton('(')));
		ParenE.createCallTransition(s1, s2, NegE);
		ParenE.createCallTransition(s1, s2, AddE);
		ParenE.createCallTransition(s1, s2, MulE);
		ParenE.createCallTransition(s1, s2, ParenE);
		ParenE.createCallTransition(s1, s2, IncrE);
		ParenE.createCallTransition(s1, s2, DecL);
		ParenE.createCallTransition(s1, s2, HexL);
		ParenE.createCallTransition(s1, s2, RealL);
		ParenE.createMatcherTransition(s2, s3, new CharSetMatcher(new CharSet().addSingleton(')')));
		ParenE = ParenE.convertNFAToDFA();
		ParenE.setLabel("ParenE");
	}

	@Test
	public void findIntersectionTest() {
		HashSet<PredictionDFA> automatons = new HashSet<PredictionDFA>();

		// automatons.add(IncrE);
		automatons.add(DecL);
		automatons.add(HexL);
		automatons.add(RealL);
		// automatons.add(AddE);
		// automatons.add(MulE);
		automatons.add(NegE);
		automatons.add(ParenE);
		// automatons.add(TerroristE);

		try {
			Utils.dumpAutomatonToFile(IncrE, desktopPath + "IncrE.graphviz");
			Utils.dumpAutomatonToFile(DecL, desktopPath + "DecL.graphviz");
			Utils.dumpAutomatonToFile(HexL, desktopPath + "HexL.graphviz");
			Utils.dumpAutomatonToFile(RealL, desktopPath + "RealL.graphviz");
			Utils.dumpAutomatonToFile(AddE, desktopPath + "AddE.graphviz");
			Utils.dumpAutomatonToFile(MulE, desktopPath + "MulE.graphviz");
			Utils.dumpAutomatonToFile(NegE, desktopPath + "NegE.graphviz");
			Utils.dumpAutomatonToFile(ParenE, desktopPath + "ParenE.graphviz");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		// IntersectionComputer ic = new IntersectionComputer(automatons);

		// assertEquals(ic.hasIntersection(), false);
		DiscriminationAutomatonComputer dc = new DiscriminationAutomatonComputer(automatons);
		dc.createDiscriminationAutomaton();

		try {
			Utils.dumpAutomatonToFile(dc.getDiscriminatorAutomaton(), desktopPath + "dc.graphviz");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void intersectionTest1() {
		// @formatter:off
		PredictionDFA a = Utils.createNFAFromSyntax(
			new MultiplicityExpr(Multiplicity.ONEORMORE, 
				new CharSetExpr(CharSet.DEC)
			)
		).convertNFAToDFA();
		a.setLabel("a");
		
		PredictionDFA b = Utils.createNFAFromSyntax(
			new SequenceExpr(
				new StringExpr("0x"),
				new MultiplicityExpr(Multiplicity.ONEORMORE, 
					new CharSetExpr(CharSet.HEX)
				)
			)
		).convertNFAToDFA();
		b.setLabel("b");
		
		PredictionDFA c = Utils.createNFAFromSyntax(
			new SequenceExpr(
				new StringExpr("0234238468273684726837462873647634"),
				new MultiplicityExpr(Multiplicity.ONEORMORE, 
					new CharSetExpr(CharSet.HEX)
				)
			)
		).convertNFAToDFA();
		c.setLabel("c");
		// @formatter:off
		
		HashSet<PredictionDFA> automatons = new HashSet<PredictionDFA>();
		
		automatons.add(a);
		automatons.add(b);
		automatons.add(c);

		IntersectionComputer ic = new IntersectionComputer(automatons);
		assertEquals(ic.hasIntersection(), true);
	}
}
