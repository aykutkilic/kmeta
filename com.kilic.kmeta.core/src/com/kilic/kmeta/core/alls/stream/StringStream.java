package com.kilic.kmeta.core.alls.stream;

public class StringStream implements IStream {
	private final String string;
	private int pos;

	public StringStream(final String string) {
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
		final String result = string.substring(pos, pos + length);
		pos += length;

		return result;
	}

	@Override
	public char lookAheadChar(final int count) {
		if (pos + count < string.length())
			return string.charAt(pos + count);
		return 0;
	}

	@Override
	public String lookAheadString(final int count, int length) {
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
	public void seek(final int pos) {
		assert (pos <= string.length());
		this.pos = pos;
	}

	@Override
	public void skip(final int count) {
		assert (pos + count <= string.length());
		this.pos += count;
	}
}
