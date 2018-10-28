package prompto.parser;

public interface IParserFactory {
	ILexer newLexer();
	IParser newParser();
}
