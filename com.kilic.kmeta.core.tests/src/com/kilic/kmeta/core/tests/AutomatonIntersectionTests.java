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
		
		IncrE = new Automaton();
		IncrE.setLabel("IncrE");
		AutomatonState startState = IncrE.createState();
		AutomatonState midState = IncrE.createState();
		AutomatonState finalState = IncrE.createState();
		
		IncrE.setStartState(startState);
		finalState.setFinal(true);
		AutomatonSetMatcher LitE = new AutomatonSetMatcher();
		IncrE.createCallTransition(startState, midState, DecL);
		IncrE.createCallTransition(startState, midState, HexL);
		IncrE.createMatcherTransition(midState, finalState, new StringMatcher("++"));
		// @formatter:on

		desktopPath = System.getProperty("user.home") + "\\Desktop\\";
	}

	@Test
	public void findIntersectionTest() {
		HashSet<Automaton> automatons = new HashSet<Automaton>();
		automatons.add(IncrE);
		automatons.add(DecL);
		automatons.add(HexL);

		try {
			Utils.dumpAutomatonToFile(IncrE, desktopPath + "IncrE.graphviz");
			Utils.dumpAutomatonToFile(DecL, desktopPath + "DecL.graphviz");
			Utils.dumpAutomatonToFile(HexL, desktopPath + "HexL.graphviz");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		IntersectionComputer ic = new IntersectionComputer(automatons);

		assertEquals(ic.hasIntersection(), false);
	}

	@Test
	public void runStateSetEqualityTest() {
		Automaton a = new Automaton();

		AutomatonState s1 = a.createState();
		AutomatonState s2 = a.createState();
		AutomatonState s3 = a.createState();

		Stack<AutomatonState> callStack1 = new Stack<>();
		Stack<AutomatonState> callStack2 = new Stack<>();

		callStack1.push(s1);
		callStack2.push(s2);
		callStack2.push(s3);

		Set<Stack<AutomatonState>> runStates1 = new HashSet<>();
		Set<Stack<AutomatonState>> runStates2 = new HashSet<>();

		runStates1.add(callStack1);
		runStates1.add(callStack2);

		runStates2.add(callStack2);
		runStates2.add(callStack1);

		assertTrue(runStates1.equals(runStates2));
	}
}
