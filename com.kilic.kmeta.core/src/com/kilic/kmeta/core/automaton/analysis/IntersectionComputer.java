package com.kilic.kmeta.core.automaton.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.kilic.kmeta.core.automaton.Automaton;
import com.kilic.kmeta.core.automaton.AutomatonRunState;
import com.kilic.kmeta.core.automaton.AutomatonState;
import com.kilic.kmeta.core.automaton.CharSetMatcher;
import com.kilic.kmeta.core.automaton.IAutomatonTransition;
import com.kilic.kmeta.core.automaton.IMatcher;
import com.kilic.kmeta.core.automaton.MatcherTransition;
import com.kilic.kmeta.core.util.CharSet;

public class IntersectionComputer {
	Set<Automaton> automatons;

	public IntersectionComputer(Set<Automaton> automatons) {
		this.automatons = automatons;
	}

	public boolean hasIntersection() {
		ParallelAutomatonRunState initState = new ParallelAutomatonRunState(automatons);
		return checkIntersections(initState, null, null);
	}

	boolean checkIntersections(ParallelAutomatonRunState currentState, List<CharSet> matcherHistory,
			Map<ParallelAutomatonRunState, List<CharSet>> stateSetHistory) {
		if (matcherHistory == null)
			matcherHistory = new ArrayList<>();

		if (stateSetHistory == null)
			stateSetHistory = new HashMap<>();

		for (CharSet cs : currentState.getAllMatchers()) {
			ArrayList<CharSet> newMatcherHistory = new ArrayList<>(matcherHistory);
			ParallelAutomatonRunState newState = new ParallelAutomatonRunState(currentState);
			//System.out.println("current State = " + newState.toString());
			//System.out.println("matchers = " + currentState.getAllMatchers().toString());
			newMatcherHistory.add(cs);
			//System.out.println("after matcher seq: " + newMatcherHistory);
			newState.applyCharSet(cs);
			//System.out.println("new State = " + newState.toString());

			int totalParallelRuns = 0;
			for (Set<AutomatonRunState> set : newState.getStates().values())
				totalParallelRuns += set.size();

			if (totalParallelRuns < 2)
				continue;

			if (newState.hasIntersection()) {
				System.out.println("intersection for matcher sequence:");
				System.out.println(newMatcherHistory);
				return true;
			}

			if (!stateSetHistory.containsKey(newState)) {
				stateSetHistory.put(newState, newMatcherHistory);
				if (checkIntersections(newState, newMatcherHistory, stateSetHistory))
					return true;
			} else {
				//System.out.println("StateSet already exists for sequence" + stateSetHistory.get(newState));
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
		for (IAutomatonTransition t : localState.getOutgoingTransitions()) {
			if (t instanceof MatcherTransition) {
				result.add((MatcherTransition) t);
			}
		}

		return result;
	}
}
