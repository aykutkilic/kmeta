package com.kilic.kmeta.core.automaton;

import com.kilic.kmeta.core.stream.IStream;

public interface IMatcher {
	boolean match(IStream stream);
}