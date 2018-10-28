package prompto.parser;

import org.antlr.v4.runtime.CharStreams;


public class MParserFactory implements IParserFactory {

	@Override
	public ILexer newLexer() {
		return new MIndentingLexer(CharStreams.fromString(""));
	}

	@Override
	public IParser newParser() {
		return new MCleverParser("");
	}

}
