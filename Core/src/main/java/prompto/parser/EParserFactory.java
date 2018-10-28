package prompto.parser;

import org.antlr.v4.runtime.CharStreams;

public class EParserFactory implements IParserFactory {

	@Override
	public ILexer newLexer() {
		return new EIndentingLexer(CharStreams.fromString(""));
	}

	@Override
	public IParser newParser() {
		return new ECleverParser("");
	}

}
