package prompto.translate.oeo;

import org.junit.Test;

import prompto.parser.o.BaseOParserTest;

public class TestNative extends BaseOParserTest {

	@Test
	public void testCategory() throws Exception {
		compareResourceOEO("native/category.poc");
	}

	@Test
	public void testCategoryReturn() throws Exception {
		compareResourceOEO("native/categoryReturn.poc");
	}

	@Test
	public void testMethod() throws Exception {
		compareResourceOEO("native/method.poc");
	}

	@Test
	public void testReturn() throws Exception {
		compareResourceOEO("native/return.poc");
	}

	@Test
	public void testReturnBooleanLiteral() throws Exception {
		compareResourceOEO("native/returnBooleanLiteral.poc");
	}

	@Test
	public void testReturnBooleanObject() throws Exception {
		compareResourceOEO("native/returnBooleanObject.poc");
	}

	@Test
	public void testReturnBooleanValue() throws Exception {
		compareResourceOEO("native/returnBooleanValue.poc");
	}

	@Test
	public void testReturnCharacterLiteral() throws Exception {
		compareResourceOEO("native/returnCharacterLiteral.poc");
	}

	@Test
	public void testReturnCharacterObject() throws Exception {
		compareResourceOEO("native/returnCharacterObject.poc");
	}

	@Test
	public void testReturnCharacterValue() throws Exception {
		compareResourceOEO("native/returnCharacterValue.poc");
	}

	@Test
	public void testReturnDecimalLiteral() throws Exception {
		compareResourceOEO("native/returnDecimalLiteral.poc");
	}

	@Test
	public void testReturnIntegerLiteral() throws Exception {
		compareResourceOEO("native/returnIntegerLiteral.poc");
	}

	@Test
	public void testReturnIntegerObject() throws Exception {
		compareResourceOEO("native/returnIntegerObject.poc");
	}

	@Test
	public void testReturnIntegerValue() throws Exception {
		compareResourceOEO("native/returnIntegerValue.poc");
	}

	@Test
	public void testReturnLongLiteral() throws Exception {
		compareResourceOEO("native/returnLongLiteral.poc");
	}

	@Test
	public void testReturnLongObject() throws Exception {
		compareResourceOEO("native/returnLongObject.poc");
	}

	@Test
	public void testReturnLongValue() throws Exception {
		compareResourceOEO("native/returnLongValue.poc");
	}

	@Test
	public void testReturnStringLiteral() throws Exception {
		compareResourceOEO("native/returnStringLiteral.poc");
	}

}

