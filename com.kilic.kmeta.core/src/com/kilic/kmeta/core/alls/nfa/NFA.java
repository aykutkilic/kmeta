package com.kilic.kmeta.core.alls.nfa;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.kilic.kmeta.core.alls.automaton.AutomatonBase;
import com.kilic.kmeta.core.alls.tokendfa.TokenDFA;
import com.kilic.kmeta.core.alls.tokendfa.TokenDFAState;
import com.kilic.kmeta.core.util.CharSet;

public class NFA extends AutomatonBase<Integer,INFAEdge, NFAState> {
	public NFAState createState() {
		return new NFAState(this);
	}
	
	public void createEpsilonEdge(NFAState from, NFAState to) {
		connectEdge(from, to, new NFAEpsilonEdge());
	}
	
	public void createCharSetEdge(NFAState from, NFAState to, CharSet charSet) {
		connectEdge(from, to, new NFACharSetEdge(charSet));
	}
	
	public TokenDFA convertToTokenDFA() {
		HashMap<EpsilonClosure, TokenDFAState> dfaStates = new HashMap<>();

		TokenDFA result = new TokenDFA();
		
		Set<EpsilonClosure> closuresToProcess = new HashSet<>();
		closuresToProcess.add(getEpsilonClosure(getStartState()));

		while (closuresToProcess.size() > 0) {
			EpsilonClosure fromClosure = closuresToProcess.iterator().next();
			TokenDFAState fromState = null;
			if (dfaStates.containsKey(fromClosure)) {
				fromState = dfaStates.get(fromClosure);
			} else
				fromState = result.createState();

			for (INFAEdge edge : fromClosure.getEdges()) {
				EpsilonClosure toClosure = fromClosure.move(edge);
				TokenDFAState toState = null;
				if (dfaStates.containsKey(toClosure)) {
					toState = dfaStates.get(toClosure);
				} else
					toState = result.createState();

				if (edge instanceof NFACharSetEdge) {

				}

			}
		}
		
		return result;
	}

	EpsilonClosure getEpsilonClosure(NFAState state) {
		EpsilonClosure result = new EpsilonClosure();
		result.add(state);

		for (INFAEdge out : state.getOut()) {
			if (out instanceof NFAEpsilonEdge)
				result.addAll(getEpsilonClosure(out.getTo()));
		}

		return result;
	}
}
