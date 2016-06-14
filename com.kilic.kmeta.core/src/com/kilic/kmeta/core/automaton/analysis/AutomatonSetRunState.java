package com.kilic.kmeta.core.automaton.analysis;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.kilic.kmeta.core.automaton.Automaton;
import com.kilic.kmeta.core.automaton.AutomatonRunState;
import com.kilic.kmeta.core.automaton.AutomatonState;
import com.kilic.kmeta.core.automaton.CallAutomatonTransition;
import com.kilic.kmeta.core.automaton.CharSetMatcher;
import com.kilic.kmeta.core.automaton.IAutomatonTransition;
import com.kilic.kmeta.core.automaton.IMatcher;
import com.kilic.kmeta.core.automaton.MatcherTransition;
import com.kilic.kmeta.core.discriminator.CharSet;

// used to keep the states of parallel execution set of automatons.
class AutomatonSetRunState {
	public Map<Automaton, Set<AutomatonRunState>> automatonSetStates = new HashMap<>();

	public AutomatonSetRunState(Set<Automaton> automatons) {
		for (Automaton a : automatons) {
			automatonSetStates.put(a, getAllStatesWithCallsAndReturns(new AutomatonRunState(a)));
		}
	}

	public AutomatonSetRunState(AutomatonSetRunState other) {
		automatonSetStates = new HashMap<>(other.automatonSetStates);
	}

	public Map<Automaton, Set<AutomatonRunState>> getStates() {
		return automatonSetStates;
	}

	public Set<AutomatonRunState> getAllStatesWithCallsAndReturns(AutomatonRunState state) {
		Set<AutomatonRunState> result = new HashSet<>();

		AutomatonState currentLocalState = state.getCurrentLocalState();
		
		// adding calls
		for (IAutomatonTransition t : currentLocalState.getOutgoingTransitions()) {
			if (t instanceof MatcherTransition) {
				result.add(state);
			}

			if (t instanceof CallAutomatonTransition) {
				AutomatonRunState newRunState = new AutomatonRunState(state);
				newRunState.call((CallAutomatonTransition) t);
				result.add(newRunState);
			}
		}

		// Adding return
		if (currentLocalState.isFinalState() && state.getCallStack().size() > 1) {
			AutomatonRunState newRunState = new AutomatonRunState( state );
			newRunState.returnFromCall();
			result.add(newRunState);
		}

		return result;
	}
	
	
	public Set<MatcherTransition> getAllMatcherTransitions() {
		Set<MatcherTransition> result = new HashSet<>();

		for (Set<AutomatonRunState> set : automatonSetStates.values()) {
			for (AutomatonRunState runState : set) {
				result.addAll(IntersectionComputer.getMatcherTransitions(runState));
			}
		}

		return result;
	}

	public void applyMatcherTransition(MatcherTransition transition) {
		Set<Automaton> deadAutomatons = new HashSet<>();

		for (Map.Entry<Automaton, Set<AutomatonRunState>> entry : this.automatonSetStates.entrySet()) {
			Set<AutomatonRunState> deadRuns = new HashSet<>();
			Set<AutomatonRunState> newRuns = new HashSet<>();
			Automaton automaton = entry.getKey();
			Set<AutomatonRunState> runStates = entry.getValue();
			IMatcher matcher = transition.getMatcher();
			
			for (AutomatonRunState runState : runStates) {
				if(matcher instanceof CharSetMatcher) {
					CharSet charSet = ((CharSetMatcher) matcher).getCharSet();
					if(IntersectionComputer.moveRunStateByIntersection(runState,charSet))
						newRuns.addAll(getAllStatesWithCallsAndReturns(runState) );
					else
						deadRuns.add(runState);
				}
			}

			runStates.addAll(newRuns);
			runStates.removeAll(deadRuns);

			if (runStates.isEmpty())
				deadAutomatons.add(automaton);
		}

		for (Automaton dead : deadAutomatons) {
			automatonSetStates.remove(dead);
		}
	}

	public boolean hasIntersection() {
		for (Set<AutomatonRunState> set : automatonSetStates.values()) {
			for (AutomatonRunState state : set) {
				if (!state.isFinal())
					return false;
			}
		}

		return true;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();

		result.append("[[\n");
		for (Entry<Automaton, Set<AutomatonRunState>> entry : automatonSetStates.entrySet()) {
			String label = entry.getKey().getLabel();
			if (label != null)
				result.append("  " + label + " {\n");
			else
				result.append("  {\n");

			for (AutomatonRunState state : entry.getValue())
				result.append("    " + state.toString() + "\n");
			result.append("  }\n");
		}

		result.append("]]\n");

		return result.toString();
	}
}
