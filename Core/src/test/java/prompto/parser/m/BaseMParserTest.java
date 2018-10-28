package prompto.parser.m;

import java.io.File;

import prompto.declaration.DeclarationList;
import prompto.utils.BaseParserTest;

public abstract class BaseMParserTest extends BaseParserTest {

	public DeclarationList parseString(String code) throws Exception {
		return super.parseMString(code);
	}
	
	public DeclarationList parseResource(String resourceName) throws Exception {
		return super.parseMResource(resourceName);
	}
	
	public DeclarationList parseFile(File file) throws Exception {
		return super.parseMFile(file);
	}

}
