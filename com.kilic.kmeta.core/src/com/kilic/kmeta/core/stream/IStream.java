package com.kilic.kmeta.core.stream;

public interface IStream {
	char nextChar();

	String getString(int length);

	char lookAheadChar();

	String lookAheadString(int length);

	void rollbackLookAhead();

	boolean hasEnded();

}
