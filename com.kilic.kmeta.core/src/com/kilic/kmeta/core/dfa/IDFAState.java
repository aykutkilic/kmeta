package com.kilic.kmeta.core.dfa;

import java.util.Set;

public interface IDFAState<SK> {
	SK getKey();
	
	Set<IDFAEdge<SK>> getIn();
	Set<IDFAEdge<SK>> getOut();
	
	boolean isFinal();
	void setFinalState(boolean finalState);
	
	boolean isErrorState();
	void setErrorState(boolean errorState);
}
