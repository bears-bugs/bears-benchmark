package prompto.translate.oeo;

import org.junit.Test;

import prompto.parser.o.BaseOParserTest;

public class TestEnums extends BaseOParserTest {

	@Test
	public void testCategoryEnum() throws Exception {
		compareResourceOEO("enums/categoryEnum.poc");
	}

	@Test
	public void testIntegerEnum() throws Exception {
		compareResourceOEO("enums/integerEnum.poc");
	}

	@Test
	public void testTextEnum() throws Exception {
		compareResourceOEO("enums/textEnum.poc");
	}

	@Test
	public void testTextEnumArg() throws Exception {
		compareResourceOEO("enums/textEnumArg.poc");
	}

	@Test
	public void testTextEnumVar() throws Exception {
		compareResourceOEO("enums/textEnumVar.poc");
	}

}

