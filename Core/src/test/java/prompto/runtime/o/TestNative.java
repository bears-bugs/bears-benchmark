package prompto.runtime.o;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.o.BaseOParserTest;
import prompto.runtime.utils.Out;

public class TestNative extends BaseOParserTest {

	@Before
	public void before() {
		Out.init();
	}

	@After
	public void after() {
		Out.restore();
	}

	@Test
	public void testInterpretedCategory() throws Exception {
		checkInterpretedOutput("native/category.poc");
	}

	@Test
	public void testCompiledCategory() throws Exception {
		checkCompiledOutput("native/category.poc");
	}

	@Test
	public void testTranspiledCategory() throws Exception {
		checkTranspiledOutput("native/category.poc");
	}

	@Test
	public void testInterpretedCategoryReturn() throws Exception {
		checkInterpretedOutput("native/categoryReturn.poc");
	}

	@Test
	public void testCompiledCategoryReturn() throws Exception {
		checkCompiledOutput("native/categoryReturn.poc");
	}

	@Test
	public void testTranspiledCategoryReturn() throws Exception {
		checkTranspiledOutput("native/categoryReturn.poc");
	}

	@Test
	public void testInterpretedMethod() throws Exception {
		checkInterpretedOutput("native/method.poc");
	}

	@Test
	public void testCompiledMethod() throws Exception {
		checkCompiledOutput("native/method.poc");
	}

	@Test
	public void testTranspiledMethod() throws Exception {
		checkTranspiledOutput("native/method.poc");
	}

	@Test
	public void testInterpretedReturnBooleanLiteral() throws Exception {
		checkInterpretedOutput("native/returnBooleanLiteral.poc");
	}

	@Test
	public void testCompiledReturnBooleanLiteral() throws Exception {
		checkCompiledOutput("native/returnBooleanLiteral.poc");
	}

	@Test
	public void testTranspiledReturnBooleanLiteral() throws Exception {
		checkTranspiledOutput("native/returnBooleanLiteral.poc");
	}

	@Test
	public void testInterpretedReturnBooleanObject() throws Exception {
		checkInterpretedOutput("native/returnBooleanObject.poc");
	}

	@Test
	public void testCompiledReturnBooleanObject() throws Exception {
		checkCompiledOutput("native/returnBooleanObject.poc");
	}

	@Test
	public void testTranspiledReturnBooleanObject() throws Exception {
		checkTranspiledOutput("native/returnBooleanObject.poc");
	}

	@Test
	public void testInterpretedReturnBooleanValue() throws Exception {
		checkInterpretedOutput("native/returnBooleanValue.poc");
	}

	@Test
	public void testCompiledReturnBooleanValue() throws Exception {
		checkCompiledOutput("native/returnBooleanValue.poc");
	}

	@Test
	public void testTranspiledReturnBooleanValue() throws Exception {
		checkTranspiledOutput("native/returnBooleanValue.poc");
	}

	@Test
	public void testInterpretedReturnCharacterLiteral() throws Exception {
		checkInterpretedOutput("native/returnCharacterLiteral.poc");
	}

	@Test
	public void testCompiledReturnCharacterLiteral() throws Exception {
		checkCompiledOutput("native/returnCharacterLiteral.poc");
	}

	@Test
	public void testTranspiledReturnCharacterLiteral() throws Exception {
		checkTranspiledOutput("native/returnCharacterLiteral.poc");
	}

	@Test
	public void testInterpretedReturnCharacterObject() throws Exception {
		checkInterpretedOutput("native/returnCharacterObject.poc");
	}

	@Test
	public void testCompiledReturnCharacterObject() throws Exception {
		checkCompiledOutput("native/returnCharacterObject.poc");
	}

	@Test
	public void testTranspiledReturnCharacterObject() throws Exception {
		checkTranspiledOutput("native/returnCharacterObject.poc");
	}

	@Test
	public void testInterpretedReturnCharacterValue() throws Exception {
		checkInterpretedOutput("native/returnCharacterValue.poc");
	}

	@Test
	public void testCompiledReturnCharacterValue() throws Exception {
		checkCompiledOutput("native/returnCharacterValue.poc");
	}

	@Test
	public void testTranspiledReturnCharacterValue() throws Exception {
		checkTranspiledOutput("native/returnCharacterValue.poc");
	}

	@Test
	public void testInterpretedReturnDecimalLiteral() throws Exception {
		checkInterpretedOutput("native/returnDecimalLiteral.poc");
	}

	@Test
	public void testCompiledReturnDecimalLiteral() throws Exception {
		checkCompiledOutput("native/returnDecimalLiteral.poc");
	}

	@Test
	public void testTranspiledReturnDecimalLiteral() throws Exception {
		checkTranspiledOutput("native/returnDecimalLiteral.poc");
	}

	@Test
	public void testInterpretedReturnIntegerLiteral() throws Exception {
		checkInterpretedOutput("native/returnIntegerLiteral.poc");
	}

	@Test
	public void testCompiledReturnIntegerLiteral() throws Exception {
		checkCompiledOutput("native/returnIntegerLiteral.poc");
	}

	@Test
	public void testTranspiledReturnIntegerLiteral() throws Exception {
		checkTranspiledOutput("native/returnIntegerLiteral.poc");
	}

	@Test
	public void testInterpretedReturnIntegerObject() throws Exception {
		checkInterpretedOutput("native/returnIntegerObject.poc");
	}

	@Test
	public void testCompiledReturnIntegerObject() throws Exception {
		checkCompiledOutput("native/returnIntegerObject.poc");
	}

	@Test
	public void testTranspiledReturnIntegerObject() throws Exception {
		checkTranspiledOutput("native/returnIntegerObject.poc");
	}

	@Test
	public void testInterpretedReturnIntegerValue() throws Exception {
		checkInterpretedOutput("native/returnIntegerValue.poc");
	}

	@Test
	public void testCompiledReturnIntegerValue() throws Exception {
		checkCompiledOutput("native/returnIntegerValue.poc");
	}

	@Test
	public void testTranspiledReturnIntegerValue() throws Exception {
		checkTranspiledOutput("native/returnIntegerValue.poc");
	}

	@Test
	public void testInterpretedReturnLongLiteral() throws Exception {
		checkInterpretedOutput("native/returnLongLiteral.poc");
	}

	@Test
	public void testCompiledReturnLongLiteral() throws Exception {
		checkCompiledOutput("native/returnLongLiteral.poc");
	}

	@Test
	public void testTranspiledReturnLongLiteral() throws Exception {
		checkTranspiledOutput("native/returnLongLiteral.poc");
	}

	@Test
	public void testInterpretedReturnLongObject() throws Exception {
		checkInterpretedOutput("native/returnLongObject.poc");
	}

	@Test
	public void testCompiledReturnLongObject() throws Exception {
		checkCompiledOutput("native/returnLongObject.poc");
	}

	@Test
	public void testTranspiledReturnLongObject() throws Exception {
		checkTranspiledOutput("native/returnLongObject.poc");
	}

	@Test
	public void testInterpretedReturnLongValue() throws Exception {
		checkInterpretedOutput("native/returnLongValue.poc");
	}

	@Test
	public void testCompiledReturnLongValue() throws Exception {
		checkCompiledOutput("native/returnLongValue.poc");
	}

	@Test
	public void testTranspiledReturnLongValue() throws Exception {
		checkTranspiledOutput("native/returnLongValue.poc");
	}

	@Test
	public void testInterpretedReturnStringLiteral() throws Exception {
		checkInterpretedOutput("native/returnStringLiteral.poc");
	}

	@Test
	public void testCompiledReturnStringLiteral() throws Exception {
		checkCompiledOutput("native/returnStringLiteral.poc");
	}

	@Test
	public void testTranspiledReturnStringLiteral() throws Exception {
		checkTranspiledOutput("native/returnStringLiteral.poc");
	}

}

