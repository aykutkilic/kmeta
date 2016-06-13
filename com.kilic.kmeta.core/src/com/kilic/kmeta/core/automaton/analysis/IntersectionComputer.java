package com.kilic.kmeta.core.automaton.analysis;

import java.util.HashSet;
import java.util.Set;

import com.kilic.kmeta.core.automaton.Automaton;
import com.kilic.kmeta.core.automaton.MatcherTransition;

public class IntersectionComputer {
	Set<Automaton> automatons;

	AutomatonSetRunState currentStates;

	public IntersectionComputer(Set<Automaton> automatons) {
		this.automatons = automatons;
		currentStates = new AutomatonSetRunState(automatons);
	}

	public boolean checkIntersections(AutomatonSetRunState currentState, Set<AutomatonSetRunState> history) {
		if (history == null)
			history = new HashSet<>();

		for (MatcherTransition t : currentState.getAllMatcherTransitions()) {
			AutomatonSetRunState newState = new AutomatonSetRunState(currentState);
			newState.applyMatcherTransition(t);

			if (newState.hasIntersection())
				return true;

			if (history.contains(newState))
				return false;

			history.add(newState);
			checkIntersections(newState, history);
		}

		return false;
	}
}
