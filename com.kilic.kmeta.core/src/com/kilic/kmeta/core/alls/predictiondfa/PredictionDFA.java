package com.kilic.kmeta.core.alls.predictiondfa;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.kilic.kmeta.core.alls.analysis.ATNConfigSet;
import com.kilic.kmeta.core.alls.analysis.RegularCallStack;
import com.kilic.kmeta.core.alls.atn.IATNEdge;
import com.kilic.kmeta.core.alls.automaton.AutomatonBase;
import com.kilic.kmeta.core.util.CharSet;

public class PredictionDFA extends AutomatonBase<ATNConfigSet, IPredictionDFAEdge, PredictionDFAState> {
	Map<IATNEdge, PredictionDFAState> finalStates;
	Map<RegularCallStack, PredictionDFAState> stackedStartStates;

	public PredictionDFA() {
		super();

		stackedStartStates = new HashMap<>();
		states = new HashMap<>();
		finalStates = new HashMap<>();
	}

	public PredictionDFAState getStartState(RegularCallStack cs) {
		return stackedStartStates.get(cs);
	}

	public void setStartState(RegularCallStack cs, PredictionDFAState state) {
		stackedStartStates.put(cs, state);
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

	public PredictionDFAEdge createEdge(PredictionDFAState from, PredictionDFAState to, CharSet matchingCharSet) {
		return new PredictionDFAEdge(from, to, matchingCharSet);
	}

	public Collection<PredictionDFAState> getFinalStates() {
		return finalStates.values();
	}

	public PredictionDFAState getFinalState(IATNEdge decisionEdge) {
		return finalStates.get(decisionEdge);
	}
}
