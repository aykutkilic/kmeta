package com.kilic.kmeta.core.tests;

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

	@Before
	public void init() {
		// @formatter:off
		DecL = Utils.createNFAFromSyntax(
			new MultiplicityExpr(Multiplicity.ONEORMORE, 
				new CharSetExpr(CharSet.DEC)
			)
		).convertNFAToDFA();
		
		HexL = Utils.createNFAFromSyntax(
			new SequenceExpr(
				new StringExpr("0x"),
				new MultiplicityExpr(Multiplicity.ONEORMORE, 
					new CharSetExpr(CharSet.HEX)
				)
			)
		).convertNFAToDFA();
		
		IncrE = new Automaton();
		AutomatonState startState = IncrE.createState();
		AutomatonState midState = IncrE.createState();
		AutomatonState finalState = IncrE.createState();
		
		IncrE.setStartState(startState);
		finalState.setFinal(true);
		AutomatonSetMatcher LitE = new AutomatonSetMatcher();
		LitE.addAutomaton(DecL);
		LitE.addAutomaton(HexL);
		IncrE.createMatcherTransition(startState, midState, LitE);
		IncrE.createMatcherTransition(midState, finalState, new StringMatcher("++"));
		// @formatter:on
	}

	@Test
	public void findIntersections() {
		HashSet<Automaton> automatons = new HashSet<Automaton>();
		automatons.add(IncrE);
		automatons.add(DecL);
		automatons.add(HexL);

		IntersectionComputer ic = new IntersectionComputer(automatons);

		assertEquals(ic.hasIntersection(), false);
	}
}
