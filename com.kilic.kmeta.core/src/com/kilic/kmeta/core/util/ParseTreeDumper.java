package com.kilic.kmeta.core.util;

import com.kilic.kmeta.core.alls.atn.ATN;
import com.kilic.kmeta.core.alls.parser.IMutator;
import com.kilic.kmeta.core.alls.parser.IParserContext;

public class ParseTreeDumper implements IParserContext {
	private int indent;
	private StringBuilder lastMatch = new StringBuilder();

	@Override
	public void onCall(final ATN atn) {
		dumpLastMatch();
		print("<" + atn.getLabel() + ">");
		indent++;
	}

	@Override
	public void onReturn(final ATN atn) {
		dumpLastMatch();
		indent--;
		print("</" + atn.getLabel() + ">");
	}

	@Override
	public void onChar(final char c) {
		lastMatch.append(c);
	}

	@Override
	public void onMutator(final IMutator mutator) {
		print("Mutate(" + mutator.getLabel() + ")");
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

	private void print(final String string) {
		final StringBuilder line = new StringBuilder();
		for (int i = 0; i < indent; i++)
			line.append(" ");

		line.append(string);
		System.out.println(line.toString());
	}
}
