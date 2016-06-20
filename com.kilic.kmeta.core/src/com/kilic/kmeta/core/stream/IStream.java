package com.kilic.kmeta.core.stream;

public interface IStream {
	char nextChar();

	String getString(int length);

	char lookAheadChar(int count);

	String lookAheadString(int count, int length);

	boolean hasEnded();

}
