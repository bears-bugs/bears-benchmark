package prompto.parser.e;

import java.io.File;

import prompto.declaration.DeclarationList;
import prompto.utils.BaseParserTest;


public abstract class BaseEParserTest extends BaseParserTest {

	public DeclarationList parseString(String code) throws Exception {
		return super.parseEString(code);
	}
	
	public DeclarationList parseResource(String resourceName) throws Exception {
		return super.parseEResource(resourceName);
	}
	
	public DeclarationList parseFile(File file) throws Exception {
		return super.parseEFile(file);
	}
	
}
