package com.kilic.kmeta.core.util;

import com.kilic.kmeta.core.alls.atn.ATN;
import com.kilic.kmeta.core.alls.parser.IParserListener;

public class ParseTreeDumper implements IParserListener {
	int indent;
	StringBuilder lastMatch = new StringBuilder();

	@Override
	public void onCall(ATN atn) {
		dumpLastMatch();
		print("<" + atn.getLabel() + ">");
		indent++;
	}

	@Override
	public void onReturn(ATN atn) {
		dumpLastMatch();
		indent--;
	}

	@Override
	public void onChar(char c) {
		lastMatch.append(c);
	}

	@Override
	public void onFinished() {
		dumpLastMatch();
	}

	private void dumpLastMatch() {
		if (lastMatch.length() > 0) {
			print(lastMatch.toString());
			lastMatch = new StringBuilder();
		}
	}

	private void print(String string) {
		StringBuilder line = new StringBuilder();
		for (int i = 0; i < indent; i++)
			line.append(" ");

		line.append(string);
		System.out.println(line.toString());
	}
}
