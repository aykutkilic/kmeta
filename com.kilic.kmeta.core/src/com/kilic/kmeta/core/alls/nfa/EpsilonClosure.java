package com.kilic.kmeta.core.alls.nfa;

import java.util.HashSet;
import java.util.Set;

public class EpsilonClosure extends HashSet<NFAState> {
	private static final long serialVersionUID = -2035118906817429220L;
	
	EpsilonClosure move(INFAEdge edge) {
		EpsilonClosure result = new EpsilonClosure();
		
		for (NFAState state : this) {
			NFAState toState = state.move(edge);
			if (toState != null)
				result.add(toState);
		}

		return result;
	}
	
	Set<INFAEdge> getEdges() {
		Set<INFAEdge> result = new HashSet<>();

		for (NFAState state : this) {
			for (INFAEdge out : state.getOut()) {
				if (out instanceof NFACharSetEdge)
					result.add(out);
			}
		}

		return result;
	}
	
}
