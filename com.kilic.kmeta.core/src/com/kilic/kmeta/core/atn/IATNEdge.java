package com.kilic.kmeta.core.atn;

public interface IATNEdge {
	ATNState getFrom();
	ATNState getTo();
}
