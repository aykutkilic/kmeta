package com.kilic.kmeta.core.alls.stream;

public class StringStream implements IStream {
	String string;

	int pos;

	public StringStream(String string) {
		this.string = string;
		this.pos = 0;
	}

	@Override
	public char nextChar() {
		if (pos >= string.length())
			return 0;

		return string.charAt(pos++);
	}

	@Override
	public String nextString(int length) {
		if (pos + length > string.length())
			length = string.length() - pos;
		String result = string.substring(pos, pos + length);
		pos += length;

		return result;
	}

	@Override
	public char lookAheadChar(int count) {
		if (pos + count < string.length())
			return string.charAt(pos + count);
		return 0;
	}

	@Override
	public String lookAheadString(int count, int length) {
		if (pos + count + length > string.length())
			length = string.length() - pos;
		return string.substring(pos + count, pos + count + length);
	}

	@Override
	public boolean hasEnded() {
		return pos >= string.length();
	}

	@Override
	public int getPosition() {
		return pos;
	}

	@Override
	public void seek(int pos) {
		assert (pos <= string.length());
		this.pos = pos;
	}

	@Override
	public void skip(int count) {
		assert (pos + count < string.length());
		this.pos += count;
	}
}
