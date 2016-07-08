package com.kilic.kmeta.core.dfa;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.kilic.kmeta.core.atn.ATNConfigSet;
import com.kilic.kmeta.core.atn.IATNEdge;
import com.kilic.kmeta.core.util.CharSet;

public class PredictionDFA extends DFABase<ATNConfigSet> {	
	Map<IATNEdge, IDFAState> finalStates;
	
	IDFAState errorState;
	
	public PredictionDFA() {
		super();
		
		states = new HashMap<>();
		finalStates = new HashMap<>();
		
		errorState = new PredictionDFAState(this, null);
		errorState.setErrorState(true);
	}
	
	public PredictionDFAState createFinalState(IATNEdge decisionEdge) {
		PredictionDFAState newState = new PredictionDFAState(this,null);
		newState.setFinal(decisionEdge);
		finalStates.put(decisionEdge, newState);
		return newState;
	}
	
	public PredictionDFAState createState(ATNConfigSet configSet) {
		PredictionDFAState newState = new PredictionDFAState(this, configSet);
		states.put(newState.stateKey, newState);
		return newState;
	}

	public void createCharSetEdge(PredictionDFAState from, PredictionDFAState to, CharSet charSet) {
		DFACharSetEdge newEdge = new DFACharSetEdge(charSet);
		newEdge.from = from;
		newEdge.to = to;
		from.out.add(newEdge);
		to.in.add(newEdge);
	}
	
	public void createStringEdge(PredictionDFAState from, PredictionDFAState to, String string) {
		DFAStringEdge newEdge = new DFAStringEdge(string);
		newEdge.from = from;
		newEdge.to = to;
		from.out.add(newEdge);
		to.in.add(newEdge);
	}

	public Collection<IDFAState> getFinalStates() {
		return finalStates.values();
	}
	
	public IDFAState getFinalState(IATNEdge decisionEdge) {
		return finalStates.get(decisionEdge);
	}
}
