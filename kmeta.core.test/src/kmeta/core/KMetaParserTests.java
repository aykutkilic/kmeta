package kmeta.core;

import static org.junit.Assert.*;

import org.junit.Test;
import org.petitparser.Parsing;
import org.petitparser.context.Result;

public class KMetaParserTests {

	@Test
	public void testKMetaParser() {
		Result result = Parsing.parse(new KMetaParser(), "package deneme;");
		assertTrue(result.isSuccess());
	}
}
