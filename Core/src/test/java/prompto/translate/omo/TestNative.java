package prompto.translate.omo;

import org.junit.Test;

import prompto.parser.o.BaseOParserTest;

public class TestNative extends BaseOParserTest {

	@Test
	public void testCategory() throws Exception {
		compareResourceOMO("native/category.poc");
	}

	@Test
	public void testCategoryReturn() throws Exception {
		compareResourceOMO("native/categoryReturn.poc");
	}

	@Test
	public void testMethod() throws Exception {
		compareResourceOMO("native/method.poc");
	}

	@Test
	public void testReturn() throws Exception {
		compareResourceOMO("native/return.poc");
	}

	@Test
	public void testReturnBooleanLiteral() throws Exception {
		compareResourceOMO("native/returnBooleanLiteral.poc");
	}

	@Test
	public void testReturnBooleanObject() throws Exception {
		compareResourceOMO("native/returnBooleanObject.poc");
	}

	@Test
	public void testReturnBooleanValue() throws Exception {
		compareResourceOMO("native/returnBooleanValue.poc");
	}

	@Test
	public void testReturnCharacterLiteral() throws Exception {
		compareResourceOMO("native/returnCharacterLiteral.poc");
	}

	@Test
	public void testReturnCharacterObject() throws Exception {
		compareResourceOMO("native/returnCharacterObject.poc");
	}

	@Test
	public void testReturnCharacterValue() throws Exception {
		compareResourceOMO("native/returnCharacterValue.poc");
	}

	@Test
	public void testReturnDecimalLiteral() throws Exception {
		compareResourceOMO("native/returnDecimalLiteral.poc");
	}

	@Test
	public void testReturnIntegerLiteral() throws Exception {
		compareResourceOMO("native/returnIntegerLiteral.poc");
	}

	@Test
	public void testReturnIntegerObject() throws Exception {
		compareResourceOMO("native/returnIntegerObject.poc");
	}

	@Test
	public void testReturnIntegerValue() throws Exception {
		compareResourceOMO("native/returnIntegerValue.poc");
	}

	@Test
	public void testReturnLongLiteral() throws Exception {
		compareResourceOMO("native/returnLongLiteral.poc");
	}

	@Test
	public void testReturnLongObject() throws Exception {
		compareResourceOMO("native/returnLongObject.poc");
	}

	@Test
	public void testReturnLongValue() throws Exception {
		compareResourceOMO("native/returnLongValue.poc");
	}

	@Test
	public void testReturnStringLiteral() throws Exception {
		compareResourceOMO("native/returnStringLiteral.poc");
	}

}

