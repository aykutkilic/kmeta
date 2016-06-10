package com.kilic.kmeta.core.tests;

import org.junit.Before;
import org.junit.Test;

import com.kilic.kmeta.core.dfa.Automaton;
import com.kilic.kmeta.core.dfa.AutomatonState;
import com.kilic.kmeta.core.dfa.StringMatcher;

public class AutomatonTests {

	Automaton nfa;

	@Before
	public void init() {
		// example in
		// http://web.cecs.pdx.edu/~harry/compilers/slides/LexicalPart3.pdf
		nfa = new Automaton();
		AutomatonState[] s = new AutomatonState[11];
		for (int i = 0; i <= 10; i++)
			s[i] = nfa.createState();

		nfa.createTransition(s[0], s[1], null);
		nfa.createTransition(s[1], s[2], null);
		nfa.createTransition(s[1], s[4], null);
		nfa.createTransition(s[1], s[7], null);
		nfa.createTransition(s[2], s[3], new StringMatcher("a"));
		nfa.createTransition(s[3], s[6], null);
		nfa.createTransition(s[4], s[5], new StringMatcher("b"));
		nfa.createTransition(s[5], s[6], null);
		nfa.createTransition(s[6], s[1], null);
		nfa.createTransition(s[6], s[7], null);
		nfa.createTransition(s[7], s[8], new StringMatcher("a"));
		nfa.createTransition(s[8], s[9], new StringMatcher("b"));
		nfa.createTransition(s[9], s[10], new StringMatcher("b"));

		nfa.setStartState(s[0]);
		s[10].setFinal(true);
	}

	@Test
	public void testNFADFAConversion() {
		System.out.println(nfa.toString());

		Automaton dfa = nfa.convertNFAToDFA();
		System.out.println(dfa.toString());
	}
}
