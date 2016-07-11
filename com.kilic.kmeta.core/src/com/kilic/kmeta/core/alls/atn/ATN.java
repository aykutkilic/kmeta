package com.kilic.kmeta.core.alls.atn;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.kilic.kmeta.core.alls.nfa.NFA;
import com.kilic.kmeta.core.alls.nfa.NFAState;
import com.kilic.kmeta.core.alls.tn.IState.StateType;
import com.kilic.kmeta.core.alls.tn.TransitionNetworkBase;
import com.kilic.kmeta.core.alls.tokendfa.TokenDFA;
import com.kilic.kmeta.core.util.CharSet;

public class ATN extends TransitionNetworkBase<Integer, IATNEdge, ATNState> {
	ATNState startState;
	ATNState finalState;
	Set<ATNCallEdge> callers;

	public ATN() {
		callers = new HashSet<>();
		startState = createState();
		finalState = createState();
		finalState.setType(StateType.FINAL);
	}

	public ATNState getStartState() {
		return startState;
	}

	public ATNState getFinalState() {
		return finalState;
	}

	public ATNState createState() {
		ATNState newState = new ATNState(this);
		states.put(newState.getKey(), newState);
		return newState;
	}

	public ATNEpsilonEdge createEpsilonEdge(ATNState from, ATNState to) {
		return new ATNEpsilonEdge(from, to);
	}

	public ATNCharSetEdge createCharSetEdge(ATNState from, ATNState to, CharSet charSet) {
		return new ATNCharSetEdge(from, to, charSet);
	}

	public ATNStringEdge createStringEdge(ATNState from, ATNState to, String string) {
		return new ATNStringEdge(from, to, string);
	}

	public ATNCallEdge createCallEdge(ATNState from, ATNState to, ATN atn) {
		return new ATNCallEdge(from, to, atn);
	}

	public ATNPredicateEdge createPredicateEdge(ATNState from, ATNState to) {
		return new ATNPredicateEdge(from, to);
	}

	public ATNMutatorEdge createMutatorEdge(ATNState from, ATNState to) {
		return new ATNMutatorEdge(from, to);
	}

	ATNTokenEdge createTokenEdge(ATNState from, ATNState to, TokenDFA dfa) {
		return new ATNTokenEdge(from, to, dfa);
	}

	public void addCaller(ATNCallEdge edge) {
		callers.add(edge);
	}

	public Set<ATNCallEdge> getAllCallers() {
		return callers;
	}

	// returns true if the ATN only contains token and epsilon edges..
	public boolean hasEquivalentNFA() {
		for (ATNState state : this.states.values()) {
			for (IATNEdge edge : state.getOut()) {
				// this can be improved to look for looping/recursive calls etc.
				if (edge instanceof ATNCallEdge)
					return false;
			}
		}

		return true;
	}

	public NFA getEquivalentNFA() {
		assert (hasEquivalentNFA());

		NFA result = new NFA();
		result.setLabel(getLabel() + "NFA");

		HashMap<ATNState, NFAState> nfaStates = new HashMap<>();

		for (ATNState state : states.values()) {
			NFAState nfaFromState = null;
			if (nfaStates.containsKey(state)) {
				nfaFromState = nfaStates.get(state);
			} else {
				nfaFromState = result.createState();
				nfaFromState.setType(state.getType());
				nfaStates.put(state, nfaFromState);
			}

			for (IATNEdge edge : state.getOut()) {
				ATNState toState = edge.getTo();

				NFAState nfaToState = null;
				if (nfaStates.containsKey(toState)) {
					nfaToState = nfaStates.get(toState);
				} else {
					nfaToState = result.createState();
					nfaToState.setType(toState.getType());
					nfaStates.put(toState, nfaToState);
				}

				if (edge instanceof ATNEpsilonEdge) {
					result.createEpsilonEdge(nfaFromState, nfaToState);
				} else if (edge instanceof ATNCharSetEdge) {
					result.createCharSetEdge(nfaFromState, nfaToState, ((ATNCharSetEdge) edge).getCharSet());
				} else if (edge instanceof ATNStringEdge) {
					String string = ((ATNStringEdge) edge).getString();

					NFAState currentNFAFromState = nfaFromState;
					for (int i = 0; i < string.length() - 1; i++) {
						NFAState currentNFAToSTate = result.createState();
						result.createCharSetEdge(currentNFAFromState, currentNFAToSTate,
								new CharSet().addSingleton(string.charAt(i)));
						currentNFAFromState = currentNFAToSTate;
					}

					result.createCharSetEdge(currentNFAFromState, nfaToState,
							new CharSet().addSingleton(string.charAt(string.length() - 1)));
				}
			}
		}

		ATNState atnStartState = getStartState();
		NFAState nfaStartState = nfaStates.get(atnStartState);

		result.setStartState(nfaStartState);

		return result;
	}

	public void reduceToTokenDFAEdge() {
		assert (hasEquivalentNFA());

		NFA nfa = getEquivalentNFA();
		TokenDFA dfa = nfa.getEquivalentDFA();
		dfa.setLabel(getLabel()+"DFA");
		
		for (ATNCallEdge edge : callers) {
			ATNState from = edge.getFrom();
			ATNState to = edge.getTo();
			edge.disconnect();
			
			ATN atn = (ATN)from.getContainer();
			atn.createTokenEdge(from, to, dfa);
		}
	}

	public String toGraphviz() {
		StringBuilder result = new StringBuilder();

		result.append("digraph finite_state_machine {\n");
		result.append("  rankdir=S;\n");
		result.append("  size=\"8,5\"\n");
		result.append("node [shape = square];\n");
		result.append("S" + startState.getKey() + ";\n");
		result.append("node [shape = doublecircle];\n ");
		result.append("S" + finalState.getKey() + ";\n");
		result.append("node [shape = circle];");

		for (ATNState state : states.values()) {
			for (IATNEdge edge : state.getOut()) {
				result.append("S" + state.getKey() + " -> S" + edge.getTo().getKey() + " [ label = \"" + edge.getLabel()
						+ "\" ];\n");
			}
		}
		result.append("}\n");

		return result.toString();
	}
}
