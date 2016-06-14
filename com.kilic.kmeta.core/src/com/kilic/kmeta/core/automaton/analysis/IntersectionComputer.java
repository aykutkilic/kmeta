package com.kilic.kmeta.core.automaton.analysis;

import java.util.HashSet;
import java.util.Set;

import com.kilic.kmeta.core.automaton.Automaton;
import com.kilic.kmeta.core.automaton.AutomatonRunState;
import com.kilic.kmeta.core.automaton.AutomatonState;
import com.kilic.kmeta.core.automaton.CharSetMatcher;
import com.kilic.kmeta.core.automaton.IAutomatonTransition;
import com.kilic.kmeta.core.automaton.ICallStackElement;
import com.kilic.kmeta.core.automaton.IMatcher;
import com.kilic.kmeta.core.automaton.MatcherTransition;
import com.kilic.kmeta.core.automaton.StringMatcher;
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

		for (MatcherTransition t : currentState.getAllMatcherTransitions()) {
			AutomatonSetRunState newState = new AutomatonSetRunState(currentState);
			newState.applyMatcherTransition(t);

			if (newState.hasIntersection())
				return true;

			if (!history.contains(newState)) {
				history.add(newState);
				checkIntersections(newState, history);
			}
		}

		return false;
	}

	// returns true if run is dead.
	public static boolean moveRunStateByIntersection(AutomatonRunState runState, CharSet input) {
		ICallStackElement localState = runState.getCurrentLocalState();
		Set<MatcherTransition> matcherTransitions = getMatcherTransitions(runState);

		if (localState instanceof AutomatonState) {
			for (MatcherTransition t : matcherTransitions) {
				IMatcher m = t.getMatcher();
				if (m instanceof CharSetMatcher) {
					CharSet charSet = ((CharSetMatcher) m).getCharSet();

					if (charSet.intersects(input))
						runState.setCurrentLocalState(t.getToState());

				} else if (m instanceof StringMatcher) {
					String stringToMatch = ((StringMatcher) m).getString();
					assert (!stringToMatch.isEmpty());

					if (input.containsSingleton(stringToMatch.charAt(0))) {
						if (stringToMatch.length() > 1) {
							runState.setCurrentLocalState(new TransitionSubState(t, 1));
						} else {
							runState.setCurrentLocalState(t.getToState());
						}
					} else
						return false;
				}
			}
		} else if (localState instanceof TransitionSubState) {
			TransitionSubState transitionSubState = (TransitionSubState) localState;
			for (MatcherTransition t : matcherTransitions) {
				IMatcher m = t.getMatcher();
				
				if (m instanceof StringMatcher) {
					String stringToMatch = ((StringMatcher) m).getString();
					int subState = transitionSubState.getSubState();
					
					if (input.containsSingleton(stringToMatch.charAt(subState))) {
						if(stringToMatch.length() == subState+1) {
							runState.setCurrentLocalState(t.getToState());
						} else 
							transitionSubState.setSubState(subState+1);
					} else
						return false;
				}
			}
		}
		
		return true;
	}

	public static Set<MatcherTransition> getMatcherTransitions(AutomatonRunState runState) {
		Set<MatcherTransition> result = new HashSet<>();

		ICallStackElement localState = runState.getCurrentLocalState();
		if (localState instanceof AutomatonState) {
			for (IAutomatonTransition t : ((AutomatonState) localState).getOutgoingTransitions()) {
				if (t instanceof MatcherTransition) {
					result.add((MatcherTransition) t);
				}
			}
		} else if (localState instanceof TransitionSubState) {
			IAutomatonTransition transition = ((TransitionSubState) localState).getTransition();
			if (transition instanceof MatcherTransition)
				result.add((MatcherTransition) transition);
		}

		return result;
	}

}
