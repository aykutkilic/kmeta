package com.kilic.kmeta.core.atn;

public interface IATNState {
	int getStateIndex();
	
	void addIn(IATNEdge edge);
	void addOut(IATNEdge edge);
}
