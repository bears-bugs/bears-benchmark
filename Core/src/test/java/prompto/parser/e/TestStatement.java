package prompto.parser.e;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;

import prompto.parser.ECleverParser;
import prompto.parser.EIndentingLexer;
import prompto.parser.EPromptoBuilder;
import prompto.statement.IStatement;

public class TestStatement {

	@Test
	public void testMethodCallWith3Arguments() {
		String code = "myMethod with 3 as x, 7 as y and 22 as 7";
		parse_statement(code);
	}
	
	IStatement parse_statement(String code) {
		ECleverParser parser = new ECleverParser(code);
		EIndentingLexer lexer = (EIndentingLexer)parser.getTokenStream().getTokenSource();
		lexer.setAddLF(false);
		ParseTree tree = parser.statement();
		EPromptoBuilder builder = new EPromptoBuilder(parser);
		ParseTreeWalker walker = new ParseTreeWalker();
		walker.walk(builder, tree);
		return builder.<IStatement>getNodeValue(tree);
	}
}
