package com.kilic.kmeta.core.tests;

import java.io.FileNotFoundException;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

import com.kilic.kmeta.core.automaton.Automaton;
import com.kilic.kmeta.core.automaton.AutomatonState;
import com.kilic.kmeta.core.automaton.StringMatcher;
import com.kilic.kmeta.core.automaton.analysis.IntersectionComputer;
import com.kilic.kmeta.core.discriminator.CharSet;
import com.kilic.kmeta.core.meta.Multiplicity;
import com.kilic.kmeta.core.syntax.CharSetExpr;
import com.kilic.kmeta.core.syntax.MultiplicityExpr;
import com.kilic.kmeta.core.syntax.SequenceExpr;
import com.kilic.kmeta.core.syntax.StringExpr;

import static org.junit.Assert.assertEquals;

public class AutomatonIntersectionTests {
	Automaton DecL;
	Automaton HexL;
	Automaton IncrE;
	Automaton AddE;
	Automaton MulE;
	Automaton NegE;
	Automaton TerroristE;

	String desktopPath;

	@Before
	public void init() {
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
			new StringExpr("0xAAA*1+2+3+6+5+4*5+++52*5")
		).convertNFAToDFA();
		TerroristE.setLabel("TerroristE");
		
		createNegE();
		createIncrE();
		createAddE();
		createMulE();
		
		// @formatter:on

		desktopPath = System.getProperty("user.home") + "\\Desktop\\";
	}

	private void createNegE() {
		NegE = new Automaton();
		NegE.setLabel("NegE");

		AutomatonState startState = NegE.createState();
		AutomatonState midState = NegE.createState();
		AutomatonState finalState = NegE.createState();

		NegE.setStartState(startState);
		finalState.setFinal(true);

		NegE.createMatcherTransition(startState, midState, new StringMatcher("+"));
		NegE.createCallTransition(midState, finalState, HexL);
		NegE.createCallTransition(midState, finalState, DecL);
	}

	private void createIncrE() {
		IncrE = new Automaton();
		IncrE.setLabel("IncrE");

		AutomatonState startState = IncrE.createState();
		AutomatonState midState = IncrE.createState();
		AutomatonState finalState = IncrE.createState();

		IncrE.setStartState(startState);
		finalState.setFinal(true);

		IncrE.createCallTransition(startState, midState, NegE);
		IncrE.createCallTransition(startState, midState, DecL);
		IncrE.createCallTransition(startState, midState, HexL);
		IncrE.createMatcherTransition(midState, finalState, new StringMatcher("++"));
	}

	private void createAddE() {
		AddE = new Automaton();

		AutomatonState s0 = AddE.createState();
		AutomatonState s1 = AddE.createState();
		AutomatonState s2 = AddE.createState();
		AutomatonState s3 = AddE.createState();

		AddE.setStartState(s0);
		s3.setFinal(true);

		AddE.createCallTransition(s0, s1, NegE);
		AddE.createCallTransition(s0, s1, DecL);
		AddE.createCallTransition(s0, s1, HexL);
		AddE.createCallTransition(s0, s1, IncrE);
		AddE.createMatcherTransition(s1, s2, new StringMatcher("+"));
		AddE.createCallTransition(s2, s3, NegE);
		AddE.createCallTransition(s2, s3, DecL);
		AddE.createCallTransition(s2, s3, HexL);
		AddE.createCallTransition(s2, s3, IncrE);
		AddE.createEpsilonTransition(s3, s1);

		AddE = AddE.convertNFAToDFA();
		AddE.setLabel("AddE");
	}

	private void createMulE() {
		MulE = new Automaton();

		AutomatonState s0 = MulE.createState();
		AutomatonState s1 = MulE.createState();
		AutomatonState s2 = MulE.createState();
		AutomatonState s3 = MulE.createState();

		MulE.setStartState(s0);
		s3.setFinal(true);

		MulE.createCallTransition(s0, s1, NegE);
		MulE.createCallTransition(s0, s1, AddE);
		MulE.createCallTransition(s0, s1, IncrE);
		MulE.createCallTransition(s0, s1, DecL);
		MulE.createCallTransition(s0, s1, HexL);
		MulE.createMatcherTransition(s1, s2, new StringMatcher("*"));
		MulE.createCallTransition(s2, s3, NegE);
		MulE.createCallTransition(s2, s3, AddE);
		MulE.createCallTransition(s2, s3, DecL);
		MulE.createCallTransition(s2, s3, HexL);
		MulE.createCallTransition(s2, s3, IncrE);
		MulE.createEpsilonTransition(s3, s1);

		MulE = MulE.convertNFAToDFA();
		MulE.setLabel("MulE");
	}

	@Test
	public void findIntersectionTest() {
		HashSet<Automaton> automatons = new HashSet<Automaton>();

		automatons.add(IncrE);
		automatons.add(DecL);
		automatons.add(HexL);
		automatons.add(AddE);
		automatons.add(MulE);
		automatons.add(NegE);
		automatons.add(TerroristE);

		try {
			Utils.dumpAutomatonToFile(IncrE, desktopPath + "IncrE.graphviz");
			Utils.dumpAutomatonToFile(DecL, desktopPath + "DecL.graphviz");
			Utils.dumpAutomatonToFile(HexL, desktopPath + "HexL.graphviz");
			Utils.dumpAutomatonToFile(AddE, desktopPath + "AddE.graphviz");
			Utils.dumpAutomatonToFile(MulE, desktopPath + "MulE.graphviz");
			Utils.dumpAutomatonToFile(NegE, desktopPath + "NegE.graphviz");
			Utils.dumpAutomatonToFile(TerroristE, desktopPath + "TerroristE.graphviz");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		IntersectionComputer ic = new IntersectionComputer(automatons);

		assertEquals(ic.hasIntersection(), false);
	}

	@Test
	public void intersectionTest1() {
		// @formatter:off
		Automaton a = Utils.createNFAFromSyntax(
			new MultiplicityExpr(Multiplicity.ONEORMORE, 
				new CharSetExpr(CharSet.DEC)
			)
		).convertNFAToDFA();
		a.setLabel("a");
		
		Automaton b = Utils.createNFAFromSyntax(
			new SequenceExpr(
				new StringExpr("0x"),
				new MultiplicityExpr(Multiplicity.ONEORMORE, 
					new CharSetExpr(CharSet.HEX)
				)
			)
		).convertNFAToDFA();
		b.setLabel("b");
		
		Automaton c = Utils.createNFAFromSyntax(
			new SequenceExpr(
				new StringExpr("0234238468273684726837462873647634"),
				new MultiplicityExpr(Multiplicity.ONEORMORE, 
					new CharSetExpr(CharSet.HEX)
				)
			)
		).convertNFAToDFA();
		c.setLabel("c");
		// @formatter:off
		
		HashSet<Automaton> automatons = new HashSet<Automaton>();
		
		automatons.add(a);
		automatons.add(b);
		automatons.add(c);

		IntersectionComputer ic = new IntersectionComputer(automatons);
		assertEquals(ic.hasIntersection(), true);
	}
}
