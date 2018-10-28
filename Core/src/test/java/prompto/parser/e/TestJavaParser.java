package prompto.parser.e;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;

import prompto.java.JavaStatement;
import prompto.parser.ECleverParser;
import prompto.parser.EIndentingLexer;
import prompto.parser.EPromptoBuilder;


public class TestJavaParser {

	@Test
	public void testReturn() throws Exception {
		String statement = "return value;";
		JavaStatement stmt = parse_java_statement(statement);
		assertNotNull(stmt);
		assertEquals(statement,stmt.toString());
	}

	@Test
	public void testExpression() throws Exception {
		String statement = "System.out;";
		JavaStatement stmt = parse_java_statement(statement);
		assertNotNull(stmt);
		assertEquals(statement,stmt.toString());
	}

	@Test
	public void testArray() throws Exception {
		String statement = "value[15];";
		JavaStatement stmt = parse_java_statement(statement);
		assertNotNull(stmt);
		assertEquals(statement,stmt.toString());
	}

	@Test
	public void testFunction() throws Exception {
		String statement = "System.out.print(value);";
		JavaStatement stmt = parse_java_statement(statement);
		assertNotNull(stmt);
		assertEquals(statement,stmt.toString());
	}
	
	JavaStatement parse_java_statement(String code) {
		ECleverParser parser = new ECleverParser(code);
		EIndentingLexer lexer = (EIndentingLexer)parser.getTokenStream().getTokenSource();
		lexer.setAddLF(false);
		ParseTree tree = parser.java_statement();
		EPromptoBuilder builder = new EPromptoBuilder(parser);
		ParseTreeWalker walker = new ParseTreeWalker();
		walker.walk(builder, tree);
		return builder.<JavaStatement>getNodeValue(tree);
	}


}