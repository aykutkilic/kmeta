package com.kilic.kmeta.core.alls.nfa;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.kilic.kmeta.core.alls.automaton.AutomatonBase;
import com.kilic.kmeta.core.alls.tokendfa.TokenDFA;
import com.kilic.kmeta.core.alls.tokendfa.TokenDFAState;
import com.kilic.kmeta.core.util.CharSet;

public class NFA extends AutomatonBase<Integer, INFAEdge, NFAState> {
	public NFAState createState() {
		NFAState newState = new NFAState(this);
		this.states.put(newState.getKey(), newState);
		return newState;
	}

	public void createEpsilonEdge(NFAState from, NFAState to) {
		connectEdge(from, to, new NFAEpsilonEdge());
	}

	public void createCharSetEdge(NFAState from, NFAState to, CharSet charSet) {
		connectEdge(from, to, new NFACharSetEdge(charSet));
	}

	public TokenDFA getEquivalentDFA() {
		HashMap<EpsilonClosure, TokenDFAState> dfaStates = new HashMap<>();

		TokenDFA result = new TokenDFA();

		Set<EpsilonClosure> closuresToProcess = new HashSet<>();
		closuresToProcess.add(EpsilonClosure.create(getStartState()));

		while (closuresToProcess.size() > 0) {
			EpsilonClosure fromClosure = closuresToProcess.iterator().next();
			TokenDFAState fromState = null;
			if (dfaStates.containsKey(fromClosure)) {
				fromState = dfaStates.get(fromClosure);
			} else {
				fromState = result.createState();
				dfaStates.put(fromClosure, fromState);
			}

			Set<CharSet> distinctCharSets = CharSet.getDistinctCharSets(fromClosure.getCharSets());

			for (CharSet charSet : distinctCharSets) {
				EpsilonClosure toClosure = fromClosure.moveByCharSet(charSet);
				closuresToProcess.add(toClosure);

				TokenDFAState toState = null;
				if (dfaStates.containsKey(toClosure)) {
					toState = dfaStates.get(toClosure);
				} else {
					toState = result.createState();
					dfaStates.put(toClosure, toState);
				}

				result.createCharSetEdge(fromState, toState, charSet);
			}
			
			closuresToProcess.remove(fromClosure);
		}

		return result;
	}
}
