package com.kilic.kmeta.core.tests;

import org.junit.Before;
import org.junit.Test;

import com.kilic.kmeta.core.automaton.Automaton;
import com.kilic.kmeta.core.automaton.AutomatonState;
import com.kilic.kmeta.core.automaton.StringMatcher;
import com.kilic.kmeta.core.discriminator.CharSet;
import com.kilic.kmeta.core.meta.Multiplicity;
import com.kilic.kmeta.core.syntax.CharSetExpr;
import com.kilic.kmeta.core.syntax.MultiplicityExpr;
import com.kilic.kmeta.core.syntax.SequenceExpr;
import com.kilic.kmeta.core.syntax.StringExpr;

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
		IncrE.createTransition(startState, midState, LitE);
		IncrE.createTransition(midState, finalState, new StringMatcher("++"));
		// @formatter:on
	}

	@Test
	public void findIntersections() {

	}
}
