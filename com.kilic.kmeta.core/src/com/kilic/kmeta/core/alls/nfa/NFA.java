package com.kilic.kmeta.core.alls.nfa;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.kilic.kmeta.core.alls.atn.ATNCharSetEdge;
import com.kilic.kmeta.core.alls.atn.ATNState;
import com.kilic.kmeta.core.alls.atn.ATNStringEdge;
import com.kilic.kmeta.core.alls.atn.IATNEdge;
import com.kilic.kmeta.core.alls.automaton.AutomatonBase;
import com.kilic.kmeta.core.alls.tokendfa.TokenDFA;
import com.kilic.kmeta.core.alls.tokendfa.TokenDFAState;

public class NFA extends AutomatonBase<Integer,INFAEdge, NFAState> {
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
