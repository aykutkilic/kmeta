package com.kilic.kmeta.core.alls.nfa;

import java.util.HashSet;
import java.util.Set;

import com.kilic.kmeta.core.alls.tn.IState.StateType;
import com.kilic.kmeta.core.util.CharSet;

public class EpsilonClosure extends HashSet<NFAState> {
	private static final long serialVersionUID = -2035118906817429220L;
	
	public static EpsilonClosure create(NFAState state) {
		EpsilonClosure result = new EpsilonClosure();
		result.add(state);

		for (INFAEdge out : state.getOut()) {
			if (out instanceof NFAEpsilonEdge)
				result.addAll(create(out.getTo()));
		}

		return result;
	}
	
	public boolean isFinal() {
		for(NFAState state : this)
			if(state.getType()==StateType.FINAL)
				return true;
		
		return false;
	}
	
	EpsilonClosure moveByCharSet(CharSet charSet) {
		EpsilonClosure result = new EpsilonClosure();
		
		for (NFAState state : this) {
			NFAState toState = state.moveByCharSet(charSet);
			if (toState != null)
				result.addAll(create(toState));
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
	
	Set<CharSet> getCharSets() {
		Set<CharSet> result = new HashSet<>();
		
		for(INFAEdge edge : getEdges()) {
			if(edge instanceof NFACharSetEdge)
				result.add(((NFACharSetEdge) edge).getCharSet());
		}
		
		return result;
	}
}
