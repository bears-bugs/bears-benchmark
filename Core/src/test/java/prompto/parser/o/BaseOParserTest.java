package prompto.parser.o;

import java.io.File;

import prompto.declaration.DeclarationList;
import prompto.utils.BaseParserTest;

public abstract class BaseOParserTest extends BaseParserTest {

	public DeclarationList parseString(String code) throws Exception {
		return super.parseOString(code);
	}
	
	public DeclarationList parseResource(String resourceName) throws Exception {
		return super.parseOResource(resourceName);
	}
	
	public DeclarationList parseFile(File file) throws Exception {
		return super.parseOFile(file);
	}

}
