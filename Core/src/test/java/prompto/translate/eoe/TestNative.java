package prompto.translate.eoe;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestNative extends BaseEParserTest {

	@Test
	public void testAnyId() throws Exception {
		compareResourceEOE("native/anyId.pec");
	}

	@Test
	public void testAnyText() throws Exception {
		compareResourceEOE("native/anyText.pec");
	}

	@Test
	public void testAttribute() throws Exception {
		compareResourceEOE("native/attribute.pec");
	}

	@Test
	public void testCategory() throws Exception {
		compareResourceEOE("native/category.pec");
	}

	@Test
	public void testCategoryReturn() throws Exception {
		compareResourceEOE("native/categoryReturn.pec");
	}

	@Test
	public void testMethod() throws Exception {
		compareResourceEOE("native/method.pec");
	}

	@Test
	public void testNow() throws Exception {
		compareResourceEOE("native/now.pec");
	}

	@Test
	public void testPrinter() throws Exception {
		compareResourceEOE("native/printer.pec");
	}

	@Test
	public void testReturn() throws Exception {
		compareResourceEOE("native/return.pec");
	}

}

