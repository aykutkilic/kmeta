package com.kilic.kmeta.core.alls.stream;

public interface IStream {
	int getPosition();

	void seek(int position);

	void skip(int count);

	char nextChar();

	String getString(int length);

	char lookAheadChar(int count);

	String lookAheadString(int count, int length);

	boolean hasEnded();
}
