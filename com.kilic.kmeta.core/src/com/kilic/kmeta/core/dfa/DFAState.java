package com.kilic.kmeta.core.dfa;

import java.util.HashSet;
import java.util.Set;

import com.kilic.kmeta.core.atn.ATNConfigSet;
import com.kilic.kmeta.core.atn.IATNEdge;
import com.kilic.kmeta.core.stream.IStream;

public class DFAState {
	enum StateType {
		REGULAR, FINAL, ERROR
	}

	ATNConfigSet configSet;
	Set<IDFAEdge> in, out;

	DFA container;
	StateType stateType;

	IATNEdge decisionEdge;

	protected DFAState(DFA container, ATNConfigSet configSet) {
		in = new HashSet<>();
		out = new HashSet<>();

		this.configSet = configSet;

		stateType = StateType.REGULAR;
		this.container = container;
	}

	public DFA getDFA() {
		return container;
	}

	public DFAState move(IStream input) {
		for (IDFAEdge edge : out) {
			if (edge.move(input))
				return edge.getTo();
		}

		return container.errorState;
	}

	public ATNConfigSet getConfigSet() {
		return configSet;
	}

	public void setFinal(IATNEdge decisionEdge) {
		stateType = decisionEdge != null ? StateType.FINAL : stateType;
		this.decisionEdge = decisionEdge;
	}

	public boolean isFinalState() {
		return stateType == StateType.FINAL;
	}

	public IATNEdge getDecisionEdge() {
		return decisionEdge;
	}

	void setErrorState(boolean isErrorState) {
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
	public String toString() {
		switch (stateType) {
		case FINAL:
			return "[[" + configSet.toString() + "]]";
		case ERROR:
			return "[E " + configSet.toString() + " E]";

		case REGULAR:
		default:
			return "[" + configSet.toString() + "]";
		}
	}

	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;

		if (!(other instanceof DFAState))
			return false;

		if (stateType != ((DFAState) other).stateType)
			return false;

		return configSet.equals(((DFAState) other).configSet);
	}

	@Override
	public int hashCode() {
		return configSet.hashCode();
	}
}
