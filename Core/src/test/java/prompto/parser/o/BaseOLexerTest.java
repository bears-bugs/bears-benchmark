package prompto.parser.o;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Token;

import prompto.parser.OLexer;
import prompto.parser.ONamingLexer;


public class BaseOLexerTest {

	public List<Token> parseTokens(Lexer lexer) {
		List<Token> result = new ArrayList<Token>();
		Token t = lexer.nextToken();
		while (t.getType()!=Token.EOF) {
			if(t.getChannel()!=Lexer.HIDDEN)
				result.add(t);
			t = lexer.nextToken();
		}
		return result;
	}

	public int[] parseTokenTypes(Lexer lexer) {
		List<Token> tokens = parseTokens(lexer);
		int[] result = new int[tokens.size()];
		int i = 0;
		for(Token t : tokens)
			result[i++] = t.getType();
		return result;
	}
	
	public String parseTokenNames(Lexer lexer) {
		List<Token> tokens = parseTokens(lexer);
		String s = "";
		for(Token t : tokens)
			s += ONamingLexer.VOCABULARY.getDisplayName(t.getType()) + " ";
		return s.substring(0,s.length()-1);
	}
	
	public Lexer newTokenStreamFromString(String input) {
		CharStream stream = CharStreams.fromString(input);
		return new OLexer(stream);
	}

	public Lexer newTokenStreamFromResource(String resourceName) {
		InputStream input = ClassLoader.getSystemClassLoader().getResourceAsStream(resourceName);
		assertNotNull(input);
		try {
			CharStream stream = CharStreams.fromStream(input);
			return new OLexer(stream);
		} catch(IOException e) {
			fail();
			return null;
		}
	}

	public String parseTokenNamesFromString(String input) {
		Lexer lexer = newTokenStreamFromString(input);
		return parseTokenNames(lexer);
	}

	public String parseTokenNamesFromResource(String input) {
		Lexer lexer = newTokenStreamFromResource(input);
		return parseTokenNames(lexer);
	}

	protected String tokenNamesAsString(int[] tokenTypes) {
		String s = "";
		for(int t : tokenTypes)
			s += ONamingLexer.VOCABULARY.getDisplayName(t)+ " ";
		return s.substring(0,s.length()-1);
	}


}
