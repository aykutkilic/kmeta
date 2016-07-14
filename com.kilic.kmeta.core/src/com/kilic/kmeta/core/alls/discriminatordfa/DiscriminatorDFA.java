package com.kilic.kmeta.core.alls.discriminatordfa;

import java.util.HashSet;
import java.util.Set;

import com.kilic.kmeta.core.alls.dfa.DFA;
import com.kilic.kmeta.core.alls.dfa.DFARunner;
import com.kilic.kmeta.core.alls.stream.IStream;
import com.kilic.kmeta.core.util.CharSet;

public class DiscriminatorDFA extends DFA {
	
	public DiscriminatorDFAFinalState createFinalState(Set<DFA> matchingDFAs) {
		DiscriminatorDFAFinalState newState = new DiscriminatorDFAFinalState(this, matchingDFAs);
		states.put(newState.getKey(), newState);
		return newState;
	}
	
	public static DiscriminatorDFA create(Set<DFA> dfas, IStream input) {
		DiscriminatorDFA result = new DiscriminatorDFA();
		
		Set<DFARunner> runners = new HashSet<>();
		for(DFA dfa : dfas)
			runners.add(new DFARunner(dfa));
		
		while(true) {
			Set<CharSet> nextCharSets = new HashSet<>();
			char c = input.nextChar();
			for(DFARunner runner : runners) {
				CharSet charSet = runner.stepMatch(c	);
				if(charSet!=null)
					nextCharSets.add(charSet);
			}
			
			Set<CharSet> distinctCharSets = CharSet.getDistinctCharSets(nextCharSets);
			
			for()
		}
		
		return result;
	} 
}
