package com.kilic.kmeta.core.automaton.analysis;

import java.util.HashSet;
import java.util.Set;

import com.kilic.kmeta.core.automaton.Automaton;
import com.kilic.kmeta.core.automaton.AutomatonRunState;
import com.kilic.kmeta.core.automaton.AutomatonState;
import com.kilic.kmeta.core.automaton.CharSetMatcher;
import com.kilic.kmeta.core.automaton.IAutomatonTransition;
import com.kilic.kmeta.core.automaton.IMatcher;
import com.kilic.kmeta.core.automaton.MatcherTransition;
import com.kilic.kmeta.core.discriminator.CharSet;

public class IntersectionComputer {
	Set<Automaton> automatons;

	public IntersectionComputer(Set<Automaton> automatons) {
		this.automatons = automatons;
	}

	public boolean hasIntersection() {
		AutomatonSetRunState initState = new AutomatonSetRunState(automatons);
		return checkIntersections(initState, null);
	}

	boolean checkIntersections(AutomatonSetRunState currentState, Set<AutomatonSetRunState> history) {
		if (history == null)
			history = new HashSet<>();

		System.out.println("current State = " + currentState.toString());
		System.out.println("matcher transitions = " + currentState.getAllMatcherTransitions().toString());
		for (MatcherTransition t : currentState.getAllMatcherTransitions()) {
			AutomatonSetRunState newState = new AutomatonSetRunState(currentState);
			newState.applyMatcherTransition(t);
			
			System.out.println("after transition :"+t.toString()); 
			System.out.println("new State = " + newState.toString());
			
			if (newState.hasIntersection())
				return true;

			if (!history.contains(newState)) {
				history.add(newState);
				checkIntersections(newState, history);
			}
		}

		return false;
	}

	// returns false if run is dead.
	public static boolean moveRunStateByIntersection(AutomatonRunState runState, CharSet input) {
		Set<MatcherTransition> matcherTransitions = getMatcherTransitions(runState);

		for (MatcherTransition t : matcherTransitions) {
			IMatcher m = t.getMatcher();
			if (m instanceof CharSetMatcher) {
				CharSet charSet = ((CharSetMatcher) m).getCharSet();

				if (charSet.intersects(input)) {
					runState.setCurrentLocalState(t.getToState());
					return true;
				}
			}
		}
		
		return false;
	}

	public static Set<MatcherTransition> getMatcherTransitions(AutomatonRunState runState) {
		Set<MatcherTransition> result = new HashSet<>();

		AutomatonState localState = runState.getCurrentLocalState();
		for (IAutomatonTransition t : ((AutomatonState) localState).getOutgoingTransitions()) {
			if (t instanceof MatcherTransition) {
				result.add((MatcherTransition) t);
			}
		}

		return result;
	}
}
