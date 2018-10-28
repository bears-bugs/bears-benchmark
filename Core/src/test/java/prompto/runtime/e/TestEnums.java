package prompto.runtime.e;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.parser.e.BaseEParserTest;
import prompto.runtime.utils.Out;

public class TestEnums extends BaseEParserTest {

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
		checkInterpretedOutput("enums/categoryEnum.pec");
	}

	@Test
	public void testCompiledCategoryEnum() throws Exception {
		checkCompiledOutput("enums/categoryEnum.pec");
	}

	@Test
	public void testTranspiledCategoryEnum() throws Exception {
		checkTranspiledOutput("enums/categoryEnum.pec");
	}

	@Test
	public void testInterpretedIntegerEnum() throws Exception {
		checkInterpretedOutput("enums/integerEnum.pec");
	}

	@Test
	public void testCompiledIntegerEnum() throws Exception {
		checkCompiledOutput("enums/integerEnum.pec");
	}

	@Test
	public void testTranspiledIntegerEnum() throws Exception {
		checkTranspiledOutput("enums/integerEnum.pec");
	}

	@Test
	public void testInterpretedStoreCategoryEnum() throws Exception {
		checkInterpretedOutput("enums/storeCategoryEnum.pec");
	}

	@Test
	public void testCompiledStoreCategoryEnum() throws Exception {
		checkCompiledOutput("enums/storeCategoryEnum.pec");
	}

	@Test
	public void testTranspiledStoreCategoryEnum() throws Exception {
		checkTranspiledOutput("enums/storeCategoryEnum.pec");
	}

	@Test
	public void testInterpretedStoreIntegerEnum() throws Exception {
		checkInterpretedOutput("enums/storeIntegerEnum.pec");
	}

	@Test
	public void testCompiledStoreIntegerEnum() throws Exception {
		checkCompiledOutput("enums/storeIntegerEnum.pec");
	}

	@Test
	public void testTranspiledStoreIntegerEnum() throws Exception {
		checkTranspiledOutput("enums/storeIntegerEnum.pec");
	}

	@Test
	public void testInterpretedStoreTextEnum() throws Exception {
		checkInterpretedOutput("enums/storeTextEnum.pec");
	}

	@Test
	public void testCompiledStoreTextEnum() throws Exception {
		checkCompiledOutput("enums/storeTextEnum.pec");
	}

	@Test
	public void testTranspiledStoreTextEnum() throws Exception {
		checkTranspiledOutput("enums/storeTextEnum.pec");
	}

	@Test
	public void testInterpretedTextEnum() throws Exception {
		checkInterpretedOutput("enums/textEnum.pec");
	}

	@Test
	public void testCompiledTextEnum() throws Exception {
		checkCompiledOutput("enums/textEnum.pec");
	}

	@Test
	public void testTranspiledTextEnum() throws Exception {
		checkTranspiledOutput("enums/textEnum.pec");
	}

	@Test
	public void testInterpretedTextEnumArg() throws Exception {
		checkInterpretedOutput("enums/textEnumArg.pec");
	}

	@Test
	public void testCompiledTextEnumArg() throws Exception {
		checkCompiledOutput("enums/textEnumArg.pec");
	}

	@Test
	public void testTranspiledTextEnumArg() throws Exception {
		checkTranspiledOutput("enums/textEnumArg.pec");
	}

	@Test
	public void testInterpretedTextEnumVar() throws Exception {
		checkInterpretedOutput("enums/textEnumVar.pec");
	}

	@Test
	public void testCompiledTextEnumVar() throws Exception {
		checkCompiledOutput("enums/textEnumVar.pec");
	}

	@Test
	public void testTranspiledTextEnumVar() throws Exception {
		checkTranspiledOutput("enums/textEnumVar.pec");
	}

}

