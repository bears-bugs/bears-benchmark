package prompto.runtime.o;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.o.BaseOParserTest;
import prompto.runtime.utils.Out;

public class TestEnums extends BaseOParserTest {

	@Before
	public void before() {
		Out.init();
	}

	@After
	public void after() {
		Out.restore();
	}

	@Test
	public void testInterpretedCategoryEnum() throws Exception {
		checkInterpretedOutput("enums/categoryEnum.poc");
	}

	@Test
	public void testCompiledCategoryEnum() throws Exception {
		checkCompiledOutput("enums/categoryEnum.poc");
	}

	@Test
	public void testTranspiledCategoryEnum() throws Exception {
		checkTranspiledOutput("enums/categoryEnum.poc");
	}

	@Test
	public void testInterpretedIntegerEnum() throws Exception {
		checkInterpretedOutput("enums/integerEnum.poc");
	}

	@Test
	public void testCompiledIntegerEnum() throws Exception {
		checkCompiledOutput("enums/integerEnum.poc");
	}

	@Test
	public void testTranspiledIntegerEnum() throws Exception {
		checkTranspiledOutput("enums/integerEnum.poc");
	}

	@Test
	public void testInterpretedTextEnum() throws Exception {
		checkInterpretedOutput("enums/textEnum.poc");
	}

	@Test
	public void testCompiledTextEnum() throws Exception {
		checkCompiledOutput("enums/textEnum.poc");
	}

	@Test
	public void testTranspiledTextEnum() throws Exception {
		checkTranspiledOutput("enums/textEnum.poc");
	}

	@Test
	public void testInterpretedTextEnumArg() throws Exception {
		checkInterpretedOutput("enums/textEnumArg.poc");
	}

	@Test
	public void testCompiledTextEnumArg() throws Exception {
		checkCompiledOutput("enums/textEnumArg.poc");
	}

	@Test
	public void testTranspiledTextEnumArg() throws Exception {
		checkTranspiledOutput("enums/textEnumArg.poc");
	}

	@Test
	public void testInterpretedTextEnumVar() throws Exception {
		checkInterpretedOutput("enums/textEnumVar.poc");
	}

	@Test
	public void testCompiledTextEnumVar() throws Exception {
		checkCompiledOutput("enums/textEnumVar.poc");
	}

	@Test
	public void testTranspiledTextEnumVar() throws Exception {
		checkTranspiledOutput("enums/textEnumVar.poc");
	}

}

