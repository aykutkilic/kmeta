package kmeta.core;

import org.petitparser.parser.CompositeParser;
import org.petitparser.parser.DelegateParser;
import org.petitparser.parser.Parser;

import static org.petitparser.Parsers.string;

public class EnclosedListParser extends DelegateParser {

	public String enclosingLeft;
	public String enclosingRight;
	public Parser childParser;

	public EnclosedListParser(Parser childParser) {
		this.enclosingLeft = "{";
		this.enclosingRight = "}";
		this.childParser = childParser;
	}

	
	public EnclosedListParser(String enclosingLeft, String enclosingRight, Parser childParser) {
		this.enclosingLeft = enclosingLeft;
		this.enclosingRight = enclosingRight;
		this.childParser = childParser;
	}

	@Override
	protected void initialize() {
		seq(string(enclosingLeft), childParser.trim().star(), string(enclosingRight));
	}
}
