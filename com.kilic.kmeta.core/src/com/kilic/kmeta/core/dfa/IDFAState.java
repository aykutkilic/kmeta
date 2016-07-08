package com.kilic.kmeta.core.dfa;

import java.util.Set;

public interface IDFAState<SK> {
	SK getStateKey();
	
	Set<IDFAEdge<SK>> getIn();
	Set<IDFAEdge<SK>> getOut();
	
	void setErrorState(boolean errorState);
}
