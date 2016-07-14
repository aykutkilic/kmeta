package com.kilic.kmeta.core.alls.discriminatordfa;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.kilic.kmeta.core.alls.atn.ATNCharSetEdge;
import com.kilic.kmeta.core.alls.atn.ATNStringEdge;
import com.kilic.kmeta.core.alls.atn.ATNTokenEdge;
import com.kilic.kmeta.core.alls.atn.IATNEdge;
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

	public static DiscriminatorDFA create(Set<IATNEdge> edges, IStream input) {
		DiscriminatorDFA result = new DiscriminatorDFA();

		Map<DFA, IATNEdge> edgeToDFA = convertEdgesToDFAs(edges);
		Set<DFARunner> runners = new HashSet<>();
		for (DFA dfa : edgeToDFA.keySet())
			runners.add(new DFARunner(dfa));

		while(true) {
			Set<CharSet> nextCharSets = new HashSet<>();
			char c = input.nextChar();
			for(DFARunner runner : runners) {
				CharSet charSet = runner.stepMatch(c);
				if(charSet!=null)
					nextCharSets.add(charSet);
			}
			
			// we don't need the whole set. first matching one is enough.
			// also it should be mapped to matching dfas/edges.
			Set<CharSet> distinctCharSets = CharSet.getDistinctCharSets(nextCharSets);
			break;
		}
		
		return result;
	}

	static Map<DFA, IATNEdge> convertEdgesToDFAs(Collection<IATNEdge> edges) {
		Map<DFA, IATNEdge> result = new HashMap<>();

		for (IATNEdge edge : edges) {
			if (edge instanceof ATNTokenEdge)
				result.put(((ATNTokenEdge) edge).getTokenDFA(), edge);
			else if (edge instanceof ATNCharSetEdge) {
				DFA dfa = DFA.createFromCharSet(((ATNCharSetEdge) edge).getCharSet());
				result.put(dfa, edge);
			} else if (edge instanceof ATNStringEdge) {
				DFA dfa = DFA.createFromString(((ATNStringEdge) edge).getString());
				result.put(dfa, edge);
			}
		}

		return result;
	}
}
