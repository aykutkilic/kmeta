package com.kilic.kmeta.core.alls.nfa;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.kilic.kmeta.core.alls.automaton.AutomatonBase;
import com.kilic.kmeta.core.alls.dfa.DFA;
import com.kilic.kmeta.core.alls.dfa.DFAState;
import com.kilic.kmeta.core.alls.tn.IState.StateType;
import com.kilic.kmeta.core.util.CharSet;

public class NFA extends AutomatonBase<Integer, INFAEdge, NFAState> {
	public NFAState createState() {
		NFAState newState = new NFAState(this);
		this.states.put(newState.getKey(), newState);
		return newState;
	}

	public NFAEpsilonEdge createEpsilonEdge(NFAState from, NFAState to) {
		return new NFAEpsilonEdge(from, to);
	}

	public NFACharSetEdge createCharSetEdge(NFAState from, NFAState to, CharSet charSet) {
		return new NFACharSetEdge(from, to, charSet);
	}

	public DFA getEquivalentDFA() {
		HashMap<EpsilonClosure, DFAState> dfaStates = new HashMap<>();

		DFA result = new DFA();

		Set<EpsilonClosure> closuresToProcess = new HashSet<>();
		closuresToProcess.add(EpsilonClosure.create(getStartState()));

		while (closuresToProcess.size() > 0) {
			EpsilonClosure fromClosure = closuresToProcess.iterator().next();
			DFAState fromState = null;
			if (dfaStates.containsKey(fromClosure)) {
				fromState = dfaStates.get(fromClosure);
			} else {
				fromState = result.createState();
				if(result.getStartState()==null)
					result.setStartState(fromState);
				if (fromClosure.isFinal())
					fromState.setType(StateType.FINAL);
				dfaStates.put(fromClosure, fromState);
			}

			Set<CharSet> distinctCharSets = CharSet.getDistinctCharSets(fromClosure.getCharSets());

			for (CharSet charSet : distinctCharSets) {
				EpsilonClosure toClosure = fromClosure.moveByCharSet(charSet);
				closuresToProcess.add(toClosure);

				DFAState toState = null;
				if (dfaStates.containsKey(toClosure)) {
					toState = dfaStates.get(toClosure);
				} else {
					toState = result.createState();
					if (toClosure.isFinal())
						toState.setType(StateType.FINAL);
					dfaStates.put(toClosure, toState);
				}

				result.createCharSetEdge(fromState, toState, charSet);
			}

			closuresToProcess.remove(fromClosure);
		}

		return result;
	}
}
