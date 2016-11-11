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
	private final Map<IATNEdge, PredictionDFAState> finalStates;
	private final Map<RegularCallStack, PredictionDFAState> stackedStartStates;

	public PredictionDFA() {
		super();

		stackedStartStates = new HashMap<>();
		finalStates = new HashMap<>();
	}

	public PredictionDFAState getStartState(final RegularCallStack cs) {
		return stackedStartStates.get(cs);
	}

	public void setStartState(final RegularCallStack cs, final PredictionDFAState state) {
		stackedStartStates.put(cs, state);
	}

	public PredictionDFAState createFinalState(final IATNEdge decisionEdge) {
		final PredictionDFAState newState = new PredictionDFAState(this, null);
		newState.setFinal(decisionEdge);
		finalStates.put(decisionEdge, newState);
		return newState;
	}

	public PredictionDFAState createState(final ATNConfigSet configSet) {
		final PredictionDFAState newState = new PredictionDFAState(this, configSet);
		states.put(newState.getKey(), newState);
		return newState;
	}

	public PredictionDFAEdge createEdge(final PredictionDFAState from, final PredictionDFAState to, final CharSet matchingCharSet) {
		return new PredictionDFAEdge(from, to, matchingCharSet);
	}

	public Collection<PredictionDFAState> getFinalStates() {
		return finalStates.values();
	}

	public PredictionDFAState getFinalState(final IATNEdge decisionEdge) {
		return finalStates.get(decisionEdge);
	}
}
