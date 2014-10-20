package kmeta.core;

import static org.petitparser.Chars.letter;

import org.petitparser.parser.CompositeParser;
import org.petitparser.parser.Parser;

import static org.petitparser.Chars.character;
import static org.petitparser.Chars.digit;
import static org.petitparser.Chars.pattern;
import static org.petitparser.Chars.whitespace;
import static org.petitparser.Chars.word;
import static org.petitparser.Parsers.string;

public class BaseGrammar extends CompositeParser {
	private static Parser IDENTIFIER = letter().seq(word().star()).flatten();
	//private static Parser NUMBER = character('-').optional()
	//		.seq(digit().plus())
	//		.seq(character('.').seq(digit().plus()).optional()).flatten();
	private static Parser STRING = character('"')
			.seq(character('"').negate().star()).seq(character('"')).flatten();

	protected Parser token(Object input) {
		Parser parser;
		if (input instanceof Parser) {
			parser = (Parser) input;
		} else if (input instanceof Character) {
			parser = character((Character) input);
		} else if (input instanceof String) {
			parser = string((String) input);
		} else {
			throw new IllegalStateException("Object not parsable: " + input);
		}
		return parser.token().trim(ref("whitespace"));
	}
	
	private void other() {
		def("whitespace", whitespace()
	        .or(ref("comment")));
	    def("comment", string("/*")
	        .seq(string("*/").negate().star())
	        .seq(string("*/")));
	}

	private void number() {
		// the original implementation uses the hand written number
		// parser of the system, this is the spec of the ANSI standard
		def("number", character('-').optional().seq(ref("positiveNumber")));
		def("positiveNumber",
				ref("scaledDecimal").or(ref("float")).or(ref("integer")));

		def("integer", ref("radixInteger").or(ref("decimalInteger")));
		def("decimalInteger", ref("digits"));
		def("digits", digit().plus());
		def("radixInteger",
				ref("radixSpecifier").seq(character('r')).seq(
						ref("radixDigits")));
		def("radixSpecifier", ref("digits"));
		def("radixDigits", pattern("0-9A-Z").plus());

		def("float",
				ref("mantissa").seq(
						ref("exponentLetter").seq(ref("exponent")).optional()));
		def("mantissa", ref("digits").seq(character('.')).seq(ref("digits")));
		def("exponent", character('-').seq(ref("decimalInteger")));
		def("exponentLetter", pattern("e"));

		def("scaledDecimal",
				ref("scaledMantissa").seq(character('s')).seq(
						ref("fractionalDigits").optional()));
		def("scaledMantissa", ref("decimalInteger").or(ref("mantissa")));
		def("fractionalDigits", ref("decimalInteger"));
	}

	private void basic() {
		def("falseLiteral", ref("falseToken"));
		def("falseToken", token("false").seq(word().not()));
		def("trueLiteral", ref("trueToken"));
		def("trueToken", token("true").seq(word().not()));
		def("identifier", IDENTIFIER);
		def("identifierToken", token(ref("identifier")));
		
		def("stringLiteral", STRING);
		def("stringToken", token(ref("stringLiteral")));
	}

	@Override
	protected void initialize() {
		other();
		number();
		basic();
	}
}
