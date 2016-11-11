package com.kilic.kmeta.core.alls.stream;

public interface IStream {
	int getPosition();
	boolean hasEnded();
	
	void seek(final int position);
	void skip(final int count);

	char nextChar();
	String nextString(final int length);

	char lookAheadChar(final int count);
	String lookAheadString(final int count, final int length);
}
