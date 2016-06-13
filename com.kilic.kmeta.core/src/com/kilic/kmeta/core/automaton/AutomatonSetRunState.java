package com.kilic.kmeta.core.automaton;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

// used to keep the states of parallel execution set of automatons.
class AutomatonSetRunState {
	public Map<Automaton, Set<AutomatonRunState>> automatonSetStates = new HashMap<>();
	
	public AutomatonSetRunState(Set<Automaton> automatons) {
		for( Automaton a : automatons ) {
			automatonSetStates.put(a, getAllStatesWithCallsAndReturn(new AutomatonRunState(a)));
		}
	}
	
	public Map<Automaton, Set<AutomatonRunState>> getStates() { return automatonSetStates; }
	
	public Set<AutomatonRunState> getAllStatesWithCallsAndReturn(AutomatonRunState state) {
		Set<AutomatonRunState> result = new HashSet<>();
		
		AutomatonState currentLocalState = state.callStack.peek();
		
		// adding calls
		for(IAutomatonTransition t : currentLocalState.out ) {
			if(t instanceof CallAutomatonTransition) {
				AutomatonRunState newRunState = (AutomatonRunState)state.callStack.clone();
				newRunState.call((CallAutomatonTransition)t);
				
				result.add(newRunState);
			}
		}
		
		
		// Adding return
		if(currentLocalState.isFinalState() && state.callStack.size()>1) {
			AutomatonRunState newRunState = (AutomatonRunState)state.callStack.clone();
			newRunState.returnFromCall();
			
			result.add(newRunState);
		}
		
		return result;
	}
	
	public Set<IMatcher> getAllMatchers() {
		Set<IMatcher> result = new HashSet<> ();
		
		for(Set<AutomatonRunState> set : automatonSetStates.values()) {
			for( AutomatonRunState runState : set) {
				AutomatonState localState = runState.callStack.peek();
				for(IAutomatonTransition t : localState.out) {
					if(t instanceof MatcherTransition) {
						result.add(((MatcherTransition) t).getMatcher());
					}
				}
			}
		}
		
		return result;
	}
}
