package kmeta.core;

import static org.petitparser.Chars.character;
import static org.petitparser.Parsers.string;

public class KMetaParser extends BaseGrammar {

	@Override
	protected void initialize() {
		super.initialize();
		def("script", ref("package").end());
		def("package",
				string("package").seq(token(ref("identifier"))).seq(
						character(';')));
	}
}
