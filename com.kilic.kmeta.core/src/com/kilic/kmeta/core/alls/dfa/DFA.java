package com.kilic.kmeta.core.alls.dfa;

import com.kilic.kmeta.core.alls.automaton.AutomatonBase;
import com.kilic.kmeta.core.alls.stream.IStream;
import com.kilic.kmeta.core.alls.tn.IState.StateType;
import com.kilic.kmeta.core.util.CharSet;

public class DFA extends AutomatonBase<Integer, IDFAEdge, DFAState> {
	public DFA() {}
	
	public DFAState createState() {
		DFAState newState = new DFAState(this);
		states.put(newState.getKey(), newState);
		return newState;
	}

	public DFACharSetEdge createCharSetEdge(DFAState from, DFAState to, CharSet charSet) {
		return new DFACharSetEdge(from, to, charSet);
	}

	public String lookAhead(IStream input) {
		int startPos = input.getPosition();
		DFARunner runner = new DFARunner(this);
		String match = runner.match(input);
		input.seek(startPos);
		return match;
	}
	
	public static DFA createFromCharSet(CharSet charSet) {
		DFA result = new DFA();
		result.setStartState( result.createState() );
		
		DFAState finalState = result.createState();
		finalState.setType(StateType.FINAL);
		
		result.createCharSetEdge(result.getStartState(), finalState, charSet);
		result.toGraphviz();
		return result;
	}
	
	public static DFA createFromString(String string) {
		assert(string.length()>0);
		
		DFA result = new DFA();
		result.setStartState( result.createState() );
		
		DFAState finalState = result.createState();
		finalState.setType(StateType.FINAL);
		DFAState currentState = result.getStartState();
		
		for (int i = 0; i < string.length() - 1; i++) {
			DFAState nextState = result.createState();
			result.createCharSetEdge(currentState, nextState, new CharSet().addSingleton(string.charAt(i)));
			currentState = nextState;
		}
		
		char lastChar = string.charAt(string.length()-1);
		result.createCharSetEdge(currentState, finalState,new CharSet().addSingleton(lastChar));
		result.toGraphviz();
		return result;
	}
}
