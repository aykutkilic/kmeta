package com.kilic.kmeta.core.automaton;

import java.util.HashSet;
import java.util.Set;

public class IntersectionComputer {
	Set<Automaton> automatons;
	
	AutomatonSetRunState currentStates;
	
	public IntersectionComputer(Set<Automaton> automatons) {
		this.automatons = automatons;
		currentStates = new AutomatonSetRunState(automatons);
	}
	
	public boolean checkIntersections() {
		Set<AutomatonSetRunState> history = new HashSet<>();
		
		//currentStates.getAllMatchers()
	}
}
