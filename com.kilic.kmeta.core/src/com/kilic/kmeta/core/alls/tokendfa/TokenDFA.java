package com.kilic.kmeta.core.alls.tokendfa;

import com.kilic.kmeta.core.alls.automaton.AutomatonBase;
import com.kilic.kmeta.core.alls.stream.IStream;
import com.kilic.kmeta.core.alls.tn.IState.StateType;
import com.kilic.kmeta.core.util.CharSet;

public class TokenDFA extends AutomatonBase<Integer, ITokenDFAEdge, TokenDFAState> {
	public TokenDFAState createState() {
		TokenDFAState newState = new TokenDFAState(this);
		states.put(newState.getKey(), newState);
		return newState;
	}

	public TokenDFACharSetEdge createCharSetEdge(TokenDFAState from, TokenDFAState to, CharSet charSet) {
		return new TokenDFACharSetEdge(from, to, charSet);
	}

	public String lookAhead(IStream input) {
		int startPos = input.getPosition();
		String match = match(input);
		input.seek(startPos);
		return match;
	}

	public String match(IStream input) {
		StringBuilder result = new StringBuilder();
		TokenDFAState current = getStartState();

		while (true) {
			boolean hasMoved = false;

			for (ITokenDFAEdge edge : current.getOut()) {
				if (edge instanceof TokenDFACharSetEdge) {
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
