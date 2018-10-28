package prompto.parser.e;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Token;

import prompto.parser.EIndentingLexer;
import prompto.parser.ELexer;
import prompto.utils.BaseTest;


public class BaseELexerTest extends BaseTest {

	public void checkTokenOffsets(String resourceName) throws Exception {
		Lexer lexer = newTokenStreamFromResource(resourceName);
		List<Token> tokens = parseTokens(lexer);
		Token previous = null;
		for(Token token : tokens) {
			checkTokenOffset(previous, token);
			previous = token;
		}
	}

	private void checkTokenOffset(Token previous, Token token) {
		if(previous==null || token.getType()==-1) // EOF
			return;
		if(token.getStartIndex()<previous.getStopIndex()+1) {
			fail("previous:" + previous.toString() + ", token:" + token.toString());
		}
	}

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
			s += ELexer.VOCABULARY.getDisplayName(t.getType()) + " ";
		return s.substring(0,s.length()-1);
	}
	
	public Lexer newTokenStreamFromString(String input) {
		CharStream stream = CharStreams.fromString(input);
		return new EIndentingLexer(stream);
	}

	public Lexer newTokenStreamFromResource(String resourceName) throws Exception {
		InputStream input = getResourceAsStream(resourceName);
		assertNotNull(input);
		CharStream stream = CharStreams.fromStream(input);
		return new EIndentingLexer(stream);
	}

	public String parseTokenNamesFromString(String input) {
		Lexer lexer = newTokenStreamFromString(input);
		return parseTokenNames(lexer);
	}

	public String parseTokenNamesFromResource(String input) throws Exception {
		Lexer lexer = newTokenStreamFromResource(input);
		return parseTokenNames(lexer);
	}

	protected String tokenNamesAsString(int[] tokenTypes) {
		String s = "";
		for(int t : tokenTypes)
			s += ELexer.VOCABULARY.getDisplayName(t)  + " ";
		return s.substring(0,s.length()-1);
	}


}
