package prompto.compiler;

import org.junit.Test;

import prompto.parser.e.BaseEParserTest;

public class TestCompilerIssues extends BaseEParserTest {

	@Test
	public void returningCursorCompilesToIterableWithCount() {
		String code = "define name as storable Text attribute\n"
				+ "define Entity as storable category with attribute name\n"
				+ "define fetchEntities as method doing:\n"
				+ "    return fetch all Entity\n"
				+ "define main as method receiving Text<:> options doing:\n"
				+ "    e1 = Entity with \"John\" as name\n"
				+ "    store e1\n"
				+ "    ee = fetchEntities\n"
				+ "    c = ee.count\n";
		executeString(code, true);
	}
	
	
	@Test
	public void returningUUIDCompiles() {
		String code = "define randomUUID as native method returning UUID doing:\n"
				+ "    Java: return java.util.UUID.randomUUID();\n\n"
				+ "define \"randomUUID is not null\" as test method doing:\n"
				+ "    uuid = randomUUID\n"
				+ "and verifying:\n"
				+ "    uuid is not nothing\n";
		executeString(code, true);
	}

}
