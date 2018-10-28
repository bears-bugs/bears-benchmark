package prompto.translate.eme;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestSetters extends BaseEParserTest {

	@Test
	public void testGetter() throws Exception {
		compareResourceEME("setters/getter.pec");
	}

	@Test
	public void testGetterCall() throws Exception {
		compareResourceEME("setters/getterCall.pec");
	}

	@Test
	public void testSetter() throws Exception {
		compareResourceEME("setters/setter.pec");
	}

}

