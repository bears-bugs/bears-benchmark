package prompto.translate.omo;

import org.junit.Test;

import prompto.parser.o.BaseOParserTest;

public class TestEnums extends BaseOParserTest {

	@Test
	public void testCategoryEnum() throws Exception {
		compareResourceOMO("enums/categoryEnum.poc");
	}

	@Test
	public void testIntegerEnum() throws Exception {
		compareResourceOMO("enums/integerEnum.poc");
	}

	@Test
	public void testTextEnum() throws Exception {
		compareResourceOMO("enums/textEnum.poc");
	}

	@Test
	public void testTextEnumArg() throws Exception {
		compareResourceOMO("enums/textEnumArg.poc");
	}

	@Test
	public void testTextEnumVar() throws Exception {
		compareResourceOMO("enums/textEnumVar.poc");
	}

}

