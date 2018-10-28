package prompto.translate.omo;

import org.junit.Test;

import prompto.parser.o.BaseOParserTest;

public class TestSetters extends BaseOParserTest {

	@Test
	public void testGetter() throws Exception {
		compareResourceOMO("setters/getter.poc");
	}

	@Test
	public void testSetter() throws Exception {
		compareResourceOMO("setters/setter.poc");
	}

}

