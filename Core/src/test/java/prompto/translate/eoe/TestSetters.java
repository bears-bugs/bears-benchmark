package prompto.translate.eoe;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestSetters extends BaseEParserTest {

	@Test
	public void testGetter() throws Exception {
		compareResourceEOE("setters/getter.pec");
	}

	@Test
	public void testGetterCall() throws Exception {
		compareResourceEOE("setters/getterCall.pec");
	}

	@Test
	public void testSetter() throws Exception {
		compareResourceEOE("setters/setter.pec");
	}

}

