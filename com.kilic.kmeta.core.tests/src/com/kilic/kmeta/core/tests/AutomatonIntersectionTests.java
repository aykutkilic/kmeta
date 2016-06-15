package com.kilic.kmeta.core.tests;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

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
import static org.junit.Assert.assertTrue;

public class AutomatonIntersectionTests {
	private static final String Set = null;
	Automaton DecL;
	Automaton HexL;
	Automaton IncrE;
	Automaton AddE;

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
		
		createIncrE();
		createAddE();
		// @formatter:on

		desktopPath = System.getProperty("user.home") + "\\Desktop\\";
	}

	private void createIncrE() {
		IncrE = new Automaton();
		IncrE.setLabel("IncrE");

		AutomatonState startState = IncrE.createState();
		AutomatonState midState = IncrE.createState();
		AutomatonState finalState = IncrE.createState();
		
		IncrE.setStartState(startState);
		finalState.setFinal(true);
		
		IncrE.createCallTransition(startState, midState, DecL);
		IncrE.createCallTransition(startState, midState, HexL);
		IncrE.createMatcherTransition(midState, finalState, new StringMatcher("++"));
	}
	
	private void createAddE() {
		AddE = new Automaton();
		AddE.setLabel("AddE");
		
		AutomatonState s0 = AddE.createState();
		AutomatonState s1 = AddE.createState();
		AutomatonState s2 = AddE.createState();
		AutomatonState s3 = AddE.createState();
		
		AddE.setStartState(s0);
		s3.setFinal(true);
		
		AddE.createCallTransition(s0, s1, DecL);
		AddE.createCallTransition(s0, s1, HexL);
		AddE.createCallTransition(s0, s1, IncrE);
		AddE.createMatcherTransition(s1, s2, new StringMatcher("+"));
		AddE.createCallTransition(s2, s3, AddE);
		AddE.createEpsilonTransition(s3, s2);
		
		AddE = AddE.convertNFAToDFA();
	}

	@Test
	public void findIntersectionTest() {
		HashSet<Automaton> automatons = new HashSet<Automaton>();
		
		automatons.add(IncrE);
		automatons.add(DecL);
		automatons.add(HexL);
		automatons.add(AddE);

		try {
			Utils.dumpAutomatonToFile(IncrE, desktopPath + "IncrE.graphviz");
			Utils.dumpAutomatonToFile(DecL, desktopPath + "DecL.graphviz");
			Utils.dumpAutomatonToFile(HexL, desktopPath + "HexL.graphviz");
			Utils.dumpAutomatonToFile(AddE, desktopPath + "AddE.graphviz");
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
