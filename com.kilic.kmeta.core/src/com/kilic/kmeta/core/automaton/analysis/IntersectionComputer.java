package com.kilic.kmeta.core.automaton.analysis;

import java.util.HashSet;
import java.util.Set;

import com.kilic.kmeta.core.automaton.Automaton;

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
