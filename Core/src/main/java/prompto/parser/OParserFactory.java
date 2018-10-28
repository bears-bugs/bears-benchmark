package prompto.parser;

import org.antlr.v4.runtime.CharStreams;

public class OParserFactory implements IParserFactory {

	@Override
	public ILexer newLexer() {
		return new ONamingLexer(CharStreams.fromString(""));
	}

	@Override
	public IParser newParser() {
		return new OCleverParser("");
	}

}
