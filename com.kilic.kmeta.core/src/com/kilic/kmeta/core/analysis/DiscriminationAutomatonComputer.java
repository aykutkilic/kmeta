package com.kilic.kmeta.core.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.kilic.kmeta.core.dfa.Automaton;
import com.kilic.kmeta.core.dfa.AutomatonRunState;
import com.kilic.kmeta.core.dfa.AutomatonState;
import com.kilic.kmeta.core.dfa.CharSetMatcher;
import com.kilic.kmeta.core.util.CharSet;

public class DiscriminationAutomatonComputer {
	Set<Automaton> automatons;
	Automaton discriminatorAutomaton;
	Map<ParallelAutomatonRunState, AutomatonState> stateSetHistory;

	public DiscriminationAutomatonComputer(Set<Automaton> automatons) {
		this.automatons = automatons;
	}

	public Automaton getDiscriminatorAutomaton() {
		return discriminatorAutomaton;
	}

	public Automaton createDiscriminationAutomaton() {
		discriminatorAutomaton = new Automaton();
		stateSetHistory = new HashMap<>();

		ParallelAutomatonRunState initState = new ParallelAutomatonRunState(automatons);
		AutomatonState initDfaState = getDfaStateForRunState(initState);
		discriminatorAutomaton.setStartState(initDfaState);
		createAutomaton(initState, null);

		return discriminatorAutomaton;
	}

	AutomatonState getDfaStateForRunState(ParallelAutomatonRunState state) {
		AutomatonState result;
		if (!stateSetHistory.containsKey(state)) {
			result = discriminatorAutomaton.createState();
			stateSetHistory.put(state, result);
		} else
			result = stateSetHistory.get(state);

		return result;
	}

	void createAutomaton(ParallelAutomatonRunState currentState, List<CharSet> matcherHistory) {
		if (matcherHistory == null)
			matcherHistory = new ArrayList<>();

		AutomatonState currentDfaState = getDfaStateForRunState(currentState);

		Set<CharSet> dcsSet = getDistinctCharSets(currentState);
		for (CharSet dcs : dcsSet) {
			ArrayList<CharSet> newMatcherHistory = new ArrayList<>(matcherHistory);
			ParallelAutomatonRunState newState = new ParallelAutomatonRunState(currentState);
			System.out.println("current State = " + newState.toString());
			System.out.println("matchers = " + dcsSet.toString());
			newMatcherHistory.add(dcs);
			System.out.println("after matcher seq: " + newMatcherHistory);
			newState.applyCharSet(dcs);
			System.out.println("new State = " + newState.toString());

			boolean newStateAlreadyExists = stateSetHistory.containsKey(newState);
			AutomatonState toDfaState = getDfaStateForRunState(newState);
			discriminatorAutomaton.createMatcherTransition(currentDfaState, toDfaState, new CharSetMatcher(dcs));

			int totalParallelRuns = 0;
			int totalFinalStates = 0;
			for (Set<AutomatonRunState> set : newState.getStates().values()) {
				totalParallelRuns += set.size();
				for (AutomatonRunState runState : set) {
					if (runState.isFinal())
						totalFinalStates++;
				}
			}

			toDfaState.setFinal(totalFinalStates == 1 || totalParallelRuns == 1);

			if (totalParallelRuns < 2)
				continue;

			if (!newStateAlreadyExists)
				createAutomaton(newState, newMatcherHistory);
		}
	}

	public Set<CharSet> getDistinctCharSets(ParallelAutomatonRunState state) {
		return CharSet.getDistinctCharSets(state.getAllMatchers());
	}
}
