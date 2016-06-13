package com.kilic.kmeta.core.automaton;

public class CallAutomatonTransition extends AutomatonTransitionBase {
	Automaton automaton;

	public CallAutomatonTransition(AutomatonState from, AutomatonState to, Automaton automaton) {
		super(from, to);
		this.automaton = automaton;
	}

	public Automaton getAutomaton() {
		return automaton;
	}

	@Override
	public boolean isEquivalent(IAutomatonTransition other) {
		if (other instanceof CallAutomatonTransition)
			return automaton == ((CallAutomatonTransition) other).automaton;

		return false;
	}

	@Override
	public String getLabel() {
		String label = automaton.getLabel();
		if (label != null)
			return label + "()";

		return automaton.getStartState() + "()";
	}
}
