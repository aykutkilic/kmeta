package com.kilic.kmeta.core.tests;

import com.kilic.kmeta.core.stream.IStream;
import com.kilic.kmeta.core.util.CharSet;

/*
 * Handwritten parser for Grammar:
 * formatter:off
 * X: E*
 * E: L | N | M | A
 * A: E [+-] E
 * M: E [/*] E
 * N: [+-] E
 * L: P | D | H | R
 * P: '(' E ')'
 * D: DEC+
 * H: 0x HEX+
 * R: DEC* '.' DEC+ ( 'e' [+-] DEC+ )?
 * formatteron
 */
public class HandWrittenParser {
	IStream stream;

	CharSet DEC = CharSet.DEC;
	CharSet HEX = CharSet.HEX;
	CharSet HEXOnly = HEX.getSubtraction(DEC);

	public HandWrittenParser(IStream stream) {

	}

	void X() {
		while (!stream.hasEnded()) {
			E();
		}
	}

	void E() {
		A();
	}

	int predictA() {
		switch (stream.lookAheadChar()) {
		case '+':
		case '-':
			return 1;
		}

		return 0;
	}

	void A() {
		M();

		switch (predictA()) {
		case 1:
			char op = stream.nextChar();
			M();
		}
	}

	int predictM() {
		switch (stream.lookAheadChar()) {
		case '*':
		case '/':
			return 1;
		}

		return 0;
	}

	void M() {
		N();

		switch (predictM()) {
		case 1:
			char op = stream.nextChar();
			N();
		}
	}

	int predictN() {
		switch (stream.lookAheadChar()) {
		case '+':
		case '-':
			return 1;
		}

		return 0;
	}

	void N() {
		switch (predictN()) {
		case 1:
			L();
			return;
		}

		L();
	}

	int predictL() {
		char c = stream.lookAheadChar();
		if (c == '(') {
			return 1;
		}

		if (c == '0') {
			c = stream.lookAheadChar();
			if (c == 'x')
				return 3;

			if (DEC.containsSingleton(c)) {
				do {
					if (stream.hasEnded())
						break;
					c = stream.lookAheadChar();
				} while (DEC.containsSingleton(c));
				if (c == '.')
					return 4;
				if (DEC.containsSingleton(c))
					return 2;
			}
		}

		if (DEC.containsSingleton(c)) {
			do {
				if (stream.hasEnded())
					break;
				c = stream.lookAheadChar();
			} while (DEC.containsSingleton(c));
			if (c == '.')
				return 4;
			if (DEC.containsSingleton(c))
				return 2;
		}

		return 0;
	}

	void L() {
		char c;

		switch (predictL()) {
		case 1:
			P();
		case 2:
			D();
		case 3:
			H();
		case 4:
			R();
		}
	}

	void P() {
		stream.nextChar(); // (
		E();
		stream.nextChar(); // )
	}

	void D() {
		char c;
		do {
			if (stream.hasEnded())
				return;
			c = stream.nextChar();
		} while (DEC.containsSingleton(c));
	}

	void H() {
		char c;
		stream.nextChar(); // 0
		stream.nextChar(); // x

		do {
			if (stream.hasEnded())
				return;
			c = stream.nextChar();
		} while (HEX.containsSingleton(c));
	}

	void R() {
		char c;
		do {
			c = stream.nextChar();
		} while (DEC.containsSingleton(c));

		stream.nextChar(); // .

		do {
			c = stream.nextChar();
		} while (DEC.containsSingleton(c));

		if (stream.lookAheadChar() == 'e') {
			stream.nextChar(); // e

			switch (stream.lookAheadChar()) {
			case '+':
			case '-':
				c = stream.nextChar(); // +-
			}

			while (DEC.containsSingleton(stream.lookAheadChar())) {
				c = stream.nextChar();
			}
		}
	}
}
