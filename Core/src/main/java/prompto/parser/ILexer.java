package prompto.parser;

import java.io.IOException;
import java.io.InputStream;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenSource;

import prompto.problem.IProblemListener;


public interface ILexer extends TokenSource {
	Dialect getDialect();
	void setProblemListener(IProblemListener listener);
	Token nextToken();
	void reset(InputStream input) throws IOException;
}
