package com.kilic.kmeta.core.tests;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.junit.Before;
import org.junit.Test;

import com.kilic.kmeta.core.automaton.Automaton;
import com.kilic.kmeta.core.automaton.AutomatonState;
import com.kilic.kmeta.core.automaton.StringMatcher;
import com.kilic.kmeta.core.discriminator.CharSet;
import com.kilic.kmeta.core.meta.Multiplicity;
import com.kilic.kmeta.core.stream.StringStream;
import com.kilic.kmeta.core.syntax.AlternativeExpr;
import com.kilic.kmeta.core.syntax.CharSetExpr;
import com.kilic.kmeta.core.syntax.ISyntaxExpr;
import com.kilic.kmeta.core.syntax.MultiplicityExpr;
import com.kilic.kmeta.core.syntax.SequenceExpr;
import com.kilic.kmeta.core.syntax.StringExpr;

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

	@Test
	public void testSyntaxExprConversion() {
		// @formatter:off
		ISyntaxExpr LetterList = new SequenceExpr(
			new StringExpr("["),
			new MultiplicityExpr(Multiplicity.OPTIONAL,
				new SequenceExpr(
					new AlternativeExpr(
						new CharSetExpr(CharSet.LETTER), 
						new StringExpr("null")
					),
					new MultiplicityExpr(Multiplicity.ANY,
						new SequenceExpr(
							new StringExpr(","), 
							new AlternativeExpr(
								new CharSetExpr(CharSet.LETTER), 
								new StringExpr("null")
							)
						)
					)
				)
			),
			new StringExpr("]")
		);
		// @formatter:on

		Automaton enfa = Utils.createNFAFromSyntax(LetterList);

		System.out.println(enfa.toString());
		System.out.println("DFA:");
		Automaton dfa = enfa.convertNFAToDFA();
		System.out.println();

		StringStream stream = new StringStream("[A,null,null,B,C,D]");
		System.out.println(dfa.match(stream));
	}

	@Test
	public void testRealLSynExprConversion() {
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

		ISyntaxExpr RealL = new AlternativeExpr(
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

		Automaton nfa = Utils.createNFAFromSyntax(RealL);
		Automaton dfa = nfa.convertNFAToDFA();
		System.out.println(dfa.toString());

		try {
			String desktopPath = System.getProperty("user.home") + "\\Desktop\\";
			dumpAutomatonToFile(dfa, desktopPath + "RealL.graphviz");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void dumpAutomatonToFile(Automaton a, String filePath) throws FileNotFoundException {
		PrintWriter out = new PrintWriter(filePath);
		out.append(a.toGraphviz());
		out.close();
	}
}
