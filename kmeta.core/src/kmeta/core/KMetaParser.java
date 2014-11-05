package kmeta.core;

import static org.petitparser.Chars.character;
import static org.petitparser.Parsers.string;

public class KMetaParser extends BaseGrammar {

	@Override
	protected void initialize() {
		super.initialize();
		def("start", ref("package").end());
		def("class", string("class").seq(token(ref("identifier"))));
		def("package",
				string("package").seq(token(ref("identifier"))).seq(
						character('{'), ref("class").star(), character('}')));
	}
}
