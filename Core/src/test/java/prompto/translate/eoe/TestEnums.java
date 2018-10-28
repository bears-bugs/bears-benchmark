package prompto.translate.eoe;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestEnums extends BaseEParserTest {

	@Test
	public void testCategoryEnum() throws Exception {
		compareResourceEOE("enums/categoryEnum.pec");
	}

	@Test
	public void testIntegerEnum() throws Exception {
		compareResourceEOE("enums/integerEnum.pec");
	}

	@Test
	public void testStoreCategoryEnum() throws Exception {
		compareResourceEOE("enums/storeCategoryEnum.pec");
	}

	@Test
	public void testStoreIntegerEnum() throws Exception {
		compareResourceEOE("enums/storeIntegerEnum.pec");
	}

	@Test
	public void testStoreTextEnum() throws Exception {
		compareResourceEOE("enums/storeTextEnum.pec");
	}

	@Test
	public void testTextEnum() throws Exception {
		compareResourceEOE("enums/textEnum.pec");
	}

	@Test
	public void testTextEnumArg() throws Exception {
		compareResourceEOE("enums/textEnumArg.pec");
	}

	@Test
	public void testTextEnumVar() throws Exception {
		compareResourceEOE("enums/textEnumVar.pec");
	}

}

