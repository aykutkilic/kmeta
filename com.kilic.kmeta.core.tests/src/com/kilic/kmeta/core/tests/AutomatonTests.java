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
	String desktopPath;

	@Before
	public void init() {
		// example in
		// http://web.cecs.pdx.edu/~harry/compilers/slides/LexicalPart3.pdf
		nfa = new Automaton();
		AutomatonState[] s = new AutomatonState[11];
		for (int i = 0; i <= 10; i++)
			s[i] = nfa.createState();

		nfa.createEpsilonTransition(s[0], s[1]);
		nfa.createEpsilonTransition(s[1], s[2]);
		nfa.createEpsilonTransition(s[1], s[4]);
		nfa.createEpsilonTransition(s[1], s[7]);
		nfa.createMatcherTransition(s[2], s[3], new StringMatcher("a"));
		nfa.createEpsilonTransition(s[3], s[6]);
		nfa.createMatcherTransition(s[4], s[5], new StringMatcher("b"));
		nfa.createEpsilonTransition(s[5], s[6]);
		nfa.createEpsilonTransition(s[6], s[1]);
		nfa.createEpsilonTransition(s[6], s[7]);
		nfa.createMatcherTransition(s[7], s[8], new StringMatcher("a"));
		nfa.createMatcherTransition(s[8], s[9], new StringMatcher("b"));
		nfa.createMatcherTransition(s[9], s[10], new StringMatcher("b"));

		nfa.setStartState(s[0]);
		s[10].setFinal(true);
		
		desktopPath = System.getProperty("user.home") + "\\Desktop\\";
	}

	@Test
	public void testNFADFAConversion() {
		System.out.println(nfa.toString());

		Automaton dfa = nfa.convertNFAToDFA();
		System.out.println(dfa.toString());
	}

	@Test
	public void testAutomatonCall() {
		// @formatter:off
		ISyntaxExpr DecL = new MultiplicityExpr(Multiplicity.ONEORMORE,
			new CharSetExpr(CharSet.DEC)
		);
		
		ISyntaxExpr HexL = new SequenceExpr(
			new StringExpr("0x"),
			new MultiplicityExpr(Multiplicity.ONEORMORE, new CharSetExpr(CharSet.HEX))
		);
		
		Automaton DecLDFA = Utils.createNFAFromSyntax(DecL).convertNFAToDFA();
		Automaton HexLDFA = Utils.createNFAFromSyntax(HexL).convertNFAToDFA();
		
		ISyntaxExpr LetterList = new SequenceExpr(
			new StringExpr("["),
			new MultiplicityExpr(Multiplicity.OPTIONAL,
				new SequenceExpr(
					new AlternativeExpr(
						new CharSetExpr(CharSet.LETTER), 
						new AutomatonCallSynExpr(DecLDFA),
						new AutomatonCallSynExpr(HexLDFA)
					),
					new MultiplicityExpr(Multiplicity.ANY,
						new SequenceExpr(
							new StringExpr(","), 
							new AlternativeExpr(
								new CharSetExpr(CharSet.LETTER), 
								new StringExpr("null"),
								new AutomatonCallSynExpr(DecLDFA),
								new AutomatonCallSynExpr(HexLDFA)
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
		System.out.println(dfa.toString());

		StringStream stream = new StringStream("[A,null,null,B,C,D]");
		//System.out.println(dfa.match(stream));
		
		try {
			dumpAutomatonToFile(dfa, desktopPath + "CallAutomaton.graphviz");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
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
