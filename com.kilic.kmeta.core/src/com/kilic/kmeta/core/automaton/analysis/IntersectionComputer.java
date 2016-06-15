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

		for (IMatcher m : currentState.getAllMatchers()) {
			AutomatonSetRunState newState = new AutomatonSetRunState(currentState);
			System.out.println("current State = " + newState.toString());
			System.out.println("matchers = " + currentState.getAllMatchers().toString());
			newState.applyMatcher(m);
			System.out.println("after matcher :" + m.toString());
			System.out.println("new State = " + newState.toString());

			int totalParallelRuns = 0;
			for (Set<AutomatonRunState> set : newState.getStates().values())
				totalParallelRuns += set.size();

			if (totalParallelRuns < 2)
				continue;

			if (newState.hasIntersection())
				return true;

			if (!history.contains(newState)) {
				history.add(newState);
				if (checkIntersections(newState, history))
					return true;
			} else 
				System.out.println("StateSet already exists");
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
		for (IAutomatonTransition t : localState.getOutgoingTransitions()) {
			if (t instanceof MatcherTransition) {
				result.add((MatcherTransition) t);
			}
		}

		return result;
	}
}
