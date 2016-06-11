package com.kilic.kmeta.core.stream;

public class StringStream implements IStream {
	String string;

	int pos;
	int lookaheadPos;

	public StringStream(String string) {
		this.string = string;
	}

	@Override
	public char nextChar() {
		if (pos > string.length())
			return string.charAt(pos++);

		return 0;
	}

	@Override
	public String getString(int length) {
		if (pos + length > string.length())
			length = string.length() - pos;
		String result = string.substring(pos, pos + length);
		pos += length;

		return result;
	}

	@Override
	public char lookAheadChar() {
		if (pos < string.length())
			return string.charAt(lookaheadPos++);

		return 0;
	}

	@Override
	public String lookAheadString(int length) {
		if (pos + length > string.length())
			length = string.length() - pos;
		String result = string.substring(pos, pos + length);
		lookaheadPos += length;

		return result;
	}

	@Override
	public void rollbackLookAhead() {
		lookaheadPos = pos;
	}

	@Override
	public boolean hasEnded() {
		// TODO Auto-generated method stub
		return false;
	}

}
