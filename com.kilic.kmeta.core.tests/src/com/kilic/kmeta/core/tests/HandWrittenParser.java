package com.kilic.kmeta.core.tests;

import com.kilic.kmeta.core.stream.IStream;
import com.kilic.kmeta.core.util.CharSet;

/*
 * Handwritten parser for Grammar:
 * formatter:off
 * X: (VAR | ABST | FN)*
 * 
 * E: L | N | I | M | A
 * A: E [+-] E
 * M: E [/*] E
 * N: [+-] E
 * I: E ('++'|'--')
 * L: P | V | D | H | R
 * P: '(' E ')'
 * V: ID
 * D: DEC+
 * H: 0x HEX+
 * R: DEC* '.' DEC+ ( 'e' [+-] DEC+ )?
 * 
 * VAR: 'var' ID ID ('=' E)?
 * ABST: 'def' ID ID '(' PARAM/',' ')' ';'
 * FN:  'def' ID ID '(' PARAM/',' ')' '{' (E ';')* '}'
 * PARAM: ID ID ('=' E)?
 * ID: LET (LET|DEC|_)*
 * 
 * formatteron
 */
public class HandWrittenParser {
	IStream stream;

	CharSet LET = CharSet.LETTER;
	CharSet DEC = CharSet.DEC;
	CharSet HEX = CharSet.HEX;
	CharSet HEXOnly = HEX.getSubtraction(DEC);

	public HandWrittenParser(IStream stream) {
		this.stream = stream;
	}

	public void parse() {
		X();
	}

	void X() {
		while (!stream.hasEnded()) {
			E();
			if (expect(';'))
				stream.nextChar();
		}
	}

	void E() {
		A();
	}

	int predictA() {
		switch (stream.lookAheadChar(0)) {
		case '+':
		case '-':
			return 1;
		}

		return 0;
	}

	void A() {
		M();

		while (predictA() == 1) {
			char op = stream.nextChar();
			System.out.print("A" + op + "]");
			M();
		}
	}

	int predictM() {
		switch (stream.lookAheadChar(0)) {
		case '*':
		case '/':
			return 1;
		}

		return 0;
	}

	void M() {
		N();

		while (predictM() == 1) {
			char op = stream.nextChar();
			System.out.print("M[" + op + "]");
			N();
		}
	}

	int predictN() {
		switch (stream.lookAheadChar(0)) {
		case '+':
		case '-':
			return 1;
		}

		return 0;
	}

	void N() {
		switch (predictN()) {
		case 1:
			char op = stream.nextChar();
			System.out.print("N[" + op + "]");
			L();
			return;
		}

		L();
	}

	int predictL() {
		int i = 0;
		char c = stream.lookAheadChar(i);
		if (c == '(') {
			return 1;
		}

		if (LET.containsSingleton(c)) {
			return 2;
		}

		if (c == '0') {
			i++;
			c = stream.lookAheadChar(i);
			if (c == 'x')
				return 4;

			if (DEC.containsSingleton(c)) {
				do {
					i++;
					c = stream.lookAheadChar(i);
				} while (c != 0 && DEC.containsSingleton(c));

				if (c == '.')
					return 5;

				return 3;
			}
		}

		if (DEC.containsSingleton(c)) {
			do {
				i++;
				c = stream.lookAheadChar(i);
			} while (c != 0 && DEC.containsSingleton(c));
			if (c == '.')
				return 5;

			return 3;
		}

		return 0;
	}

	void L() {
		switch (predictL()) {
		case 1:
			P();
			break;
		case 2:
			V();
			break;
		case 3:
			D();
			break;
		case 4:
			H();
			break;
		case 5:
			R();
			break;
		default:
			System.out.println("Syntax Error = can not match L");
			stream.nextChar();
		}
	}

	void P() {
		System.out.print("P");
		stream.nextChar(); // (
		E();
		if (expect(')'))
			stream.nextChar(); // )
	}

	void V() {
		StringBuilder d = new StringBuilder();

		char c;
		while (LET.containsSingleton(stream.lookAheadChar(0))) {
			c = stream.nextChar();
			d.append(c);
		}

		System.out.print(d.toString());
	}

	void D() {
		StringBuilder d = new StringBuilder();
		char c;
		while (DEC.containsSingleton(stream.lookAheadChar(0))) {
			c = stream.nextChar();
			d.append(c);
		}

		System.out.print(d.toString());
	}

	void H() {
		StringBuilder h = new StringBuilder();
		char c;
		c = stream.nextChar(); // 0
		c = stream.nextChar(); // x

		h.append("0x");
		expect(HEX);
		while (HEX.containsSingleton(stream.lookAheadChar(0))) {
			c = stream.nextChar();
			h.append(c);
		}

		System.out.print(h.toString());
	}

	void R() {
		StringBuilder r = new StringBuilder();
		char c;
		while (DEC.containsSingleton(stream.lookAheadChar(0))) {
			c = stream.nextChar();
			r.append(c);
		}

		c = stream.nextChar(); // .
		r.append(c);

		while (DEC.containsSingleton(stream.lookAheadChar(0))) {
			c = stream.nextChar();
			r.append(c);
		}

		if (stream.lookAheadChar(0) == 'e') {
			c = stream.nextChar(); // e
			r.append(c);

			switch (stream.lookAheadChar(0)) {
			case '+':
			case '-':
				c = stream.nextChar(); // +-
				r.append(c);
			}

			while (DEC.containsSingleton(stream.lookAheadChar(0))) {
				c = stream.nextChar();
				r.append(c);
			}
		}

		System.out.print(r.toString());
	}

	boolean expect(char c) {
		return expect(new CharSet().addSingleton(c));
	}

	boolean expect(CharSet cs) {
		char c = stream.lookAheadChar(0);

		if (!cs.containsSingleton(c)) {
			System.out.println("Mismatched input '" + c + "'. Expecting " + cs.toString());
			return false;
		}

		return true;
	}
}
