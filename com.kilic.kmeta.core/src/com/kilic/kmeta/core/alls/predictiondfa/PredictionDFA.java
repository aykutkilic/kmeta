package com.kilic.kmeta.core.alls.predictiondfa;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.kilic.kmeta.core.alls.analysis.ATNConfigSet;
import com.kilic.kmeta.core.alls.atn.IATNEdge;
import com.kilic.kmeta.core.alls.dfa.DFABase;
import com.kilic.kmeta.core.util.CharSet;

public class PredictionDFA extends DFABase<ATNConfigSet, PredictionDFAEdge, PredictionDFAState> {
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
		states.put(newState.key, newState);
		return newState;
	}

	public void createCharSetEdge(PredictionDFAState from, PredictionDFAState to, CharSet charSet) {
		DFACharSetEdge<ATNConfigSet> newEdge = new DFACharSetEdge<>(charSet);
		newEdge.from = from;
		newEdge.to = to;
		from.out.add(newEdge);
		to.in.add(newEdge);
	}

	public void createStringEdge(PredictionDFAState from, PredictionDFAState to, String string) {
		DFAStringEdge<ATNConfigSet> newEdge = new DFAStringEdge<>(string);
		newEdge.from = from;
		newEdge.to = to;
		from.out.add(newEdge);
		to.in.add(newEdge);
	}

	public Collection<PredictionDFAState> getFinalStates() {
		return finalStates.values();
	}

	public PredictionDFAState getFinalState(IATNEdge decisionEdge) {
		return finalStates.get(decisionEdge);
	}
}
