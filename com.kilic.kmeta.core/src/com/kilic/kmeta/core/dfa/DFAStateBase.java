package com.kilic.kmeta.core.dfa;

import java.util.HashSet;
import java.util.Set;

import com.kilic.kmeta.core.atn.ATNConfigSet;

public class DFAStateBase<SK> implements IDFAState<SK> {
	enum StateType {
		REGULAR, FINAL, ERROR
	}

	SK stateKey;
	Set<IDFAEdge> in, out;

	PredictionDFA container;
	StateType stateType;

	protected DFAStateBase( PredictionDFA container, SK stateKey ) {
		in = new HashSet<>();
		out = new HashSet<>();
		
		this.container = container;
		this.stateKey = stateKey;
	}
	
	public PredictionDFA getDFA() {
		return container;
	}

	public void setFinal(boolean isFinal) {
		stateType = isFinal ? StateType.FINAL : stateType;
	}

	public boolean isFinalState() {
		return stateType == StateType.FINAL;
	}

	public SK getKey() {
		return stateKey;
	}
	
	public void setErrorState(boolean isErrorState) {
		stateType = isErrorState ? StateType.ERROR : stateType;
	}

	public boolean isErrorState() {
		return stateType == StateType.ERROR;
	}

	public void addIn(IDFAEdge edge) {
		in.add(edge);
	}

	public void addOut(IDFAEdge edge) {
		out.add(edge);
	}

	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;

		if (!(other instanceof DFAStateBase<?>))
			return false;

		if (stateType != ((DFAStateBase<?>) other).stateType)
			return false;

		return stateKey.equals(((PredictionDFAState) other).stateKey);
	}
	
	@Override
	public String toString() {
		switch (stateType) {
		case FINAL:
			return "[[" + stateKey.toString() + "]]";
		case ERROR:
			return "[E " + stateKey.toString() + " E]";

		case REGULAR:
		default:
			return "[" + stateKey.toString() + "]";
		}
	}

	@Override
	public int hashCode() {
		return stateKey.hashCode();
	}
}
