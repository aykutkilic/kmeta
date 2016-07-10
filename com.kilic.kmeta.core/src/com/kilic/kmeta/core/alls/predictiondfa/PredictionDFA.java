package com.kilic.kmeta.core.alls.predictiondfa;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.kilic.kmeta.core.alls.analysis.ATNConfigSet;
import com.kilic.kmeta.core.alls.atn.IATNEdge;
import com.kilic.kmeta.core.alls.automaton.AutomatonBase;
import com.kilic.kmeta.core.util.CharSet;

public class PredictionDFA extends AutomatonBase<ATNConfigSet, IPredictionDFAEdge, PredictionDFAState> {
	Map<IATNEdge, PredictionDFAState> finalStates;

	public PredictionDFA() {
		super();

		states = new HashMap<>();
		finalStates = new HashMap<>();
	}

	public PredictionDFAState createFinalState(IATNEdge decisionEdge) {
		PredictionDFAState newState = new PredictionDFAState(this, null);
		newState.setFinal(decisionEdge);
		finalStates.put(decisionEdge, newState);
		return newState;
	}

	public PredictionDFAState createState(ATNConfigSet configSet) {
		PredictionDFAState newState = new PredictionDFAState(this, configSet);
		states.put(newState.getKey(), newState);
		return newState;
	}

	public void createCharSetEdge(PredictionDFAState from, PredictionDFAState to, CharSet charSet) {
		connectEdge(from, to, new PredictionDFACharSetEdge(charSet));
	}

	public void createStringEdge(PredictionDFAState from, PredictionDFAState to, String string) {
		connectEdge(from, to, new PredictionDFAStringEdge(string));
	}

	public Collection<PredictionDFAState> getFinalStates() {
		return finalStates.values();
	}

	public PredictionDFAState getFinalState(IATNEdge decisionEdge) {
		return finalStates.get(decisionEdge);
	}
}
