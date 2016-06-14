package com.kilic.kmeta.core.automaton.analysis;

import com.kilic.kmeta.core.automaton.IAutomatonTransition;
import com.kilic.kmeta.core.automaton.ICallStackElement;
import com.kilic.kmeta.core.automaton.IMatcher;
import com.kilic.kmeta.core.automaton.MatcherTransition;
import com.kilic.kmeta.core.automaton.StringMatcher;

public class TransitionSubState implements ICallStackElement {
	final IAutomatonTransition transition;
	int subState = 0;
	
	public TransitionSubState(IAutomatonTransition transition, int subState) {
		this.transition = transition;
		this.subState = subState;
	}
	
	public void setSubState(int newSubState) {
		subState = newSubState;
	}
	
	public int getSubState() { return subState; }
	
	public IAutomatonTransition getTransition() { return transition; }

	public ICallStackElement move(MatcherTransition transition) {
		if(this.transition instanceof MatcherTransition) {
			MatcherTransition matcherTransition = (MatcherTransition) this.transition;
			IMatcher matcher = matcherTransition.getMatcher();
			if(matcher instanceof StringMatcher) {
				StringMatcher stringMatcher = (StringMatcher) matcher;
			}
		}
		return null;
	}
}
