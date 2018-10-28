package prompto.parser;

import java.io.InputStream;

import prompto.declaration.DeclarationList;
import prompto.problem.IProblemListener;

public interface IParser {

	ILexer getLexer();
	void setProblemListener(IProblemListener listener);
	DeclarationList parse(String path, InputStream data) throws Exception;
}
