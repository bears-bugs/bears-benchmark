package prompto.translate.eme;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestNative extends BaseEParserTest {

	@Test
	public void testAnyId() throws Exception {
		compareResourceEME("native/anyId.pec");
	}

	@Test
	public void testAnyText() throws Exception {
		compareResourceEME("native/anyText.pec");
	}

	@Test
	public void testAttribute() throws Exception {
		compareResourceEME("native/attribute.pec");
	}

	@Test
	public void testCategory() throws Exception {
		compareResourceEME("native/category.pec");
	}

	@Test
	public void testCategoryReturn() throws Exception {
		compareResourceEME("native/categoryReturn.pec");
	}

	@Test
	public void testMethod() throws Exception {
		compareResourceEME("native/method.pec");
	}

	@Test
	public void testNow() throws Exception {
		compareResourceEME("native/now.pec");
	}

	@Test
	public void testPrinter() throws Exception {
		compareResourceEME("native/printer.pec");
	}

	@Test
	public void testReturn() throws Exception {
		compareResourceEME("native/return.pec");
	}

}

