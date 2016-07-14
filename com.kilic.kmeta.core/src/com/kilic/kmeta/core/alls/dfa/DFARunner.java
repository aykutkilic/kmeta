package com.kilic.kmeta.core.alls.dfa;

import com.kilic.kmeta.core.alls.stream.IStream;
import com.kilic.kmeta.core.alls.tn.IState.StateType;
import com.kilic.kmeta.core.util.CharSet;

public class DFARunner {
	DFA dfa;
	DFAState current;
	
	public DFARunner(DFA dfa) {
		this.dfa = dfa;
		current = dfa.getStartState();
	}
	
	public DFA getDFA() {
		return dfa;
	}
	
	public DFAState getCurrentState() {
		return current;
	}
	
	// returns the CharSet of matching edge if any
	public CharSet stepMatch(char c) {
		for (IDFAEdge edge : current.getOut()) {
			CharSet cs = ((DFACharSetEdge)edge).getCharSet();
			if(cs.containsSingleton(c))
				return cs;
		}

		return null;
	}
	
	public String match(IStream input) {
		StringBuilder result = new StringBuilder();

		current = dfa.getStartState();
		while (true) {
			boolean hasMoved = false;

			for (IDFAEdge edge : current.getOut()) {
				if (edge instanceof DFACharSetEdge) {
					if (edge.moves(input)) {
						result.append(input.nextChar());
						current = edge.getTo();
						hasMoved = true;
						break;
					}
				} else {
					return null;
				}
			}

			if (!hasMoved) {
				if (current.getType() == StateType.FINAL)
					return result.toString();

				return null;
			}
		}
	}
}
