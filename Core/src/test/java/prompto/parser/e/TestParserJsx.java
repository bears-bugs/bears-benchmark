package prompto.parser.e;

import static org.junit.Assert.*;

import org.junit.Test;

import prompto.parser.Dialect;
import prompto.parser.ECleverParser;
import prompto.runtime.Context;
import prompto.statement.ReturnStatement;
import prompto.utils.CodeWriter;

public class TestParserJsx extends BaseEParserTest {

	@Test
	public void canParseAndTranslateMultilineElements() {
		String jsx = "return <a>\n\t<b/>\n\t<b/>\n</a>";
		ECleverParser parser = new ECleverParser(jsx);
		ReturnStatement stmt = parser.<ReturnStatement>doParse(parser::return_statement, true);
		assertNotNull(stmt.getExpression());
		CodeWriter writer = new CodeWriter(Dialect.E, Context.newGlobalContext());
		stmt.toDialect(writer);
		String out = writer.toString();
		assertEquals(jsx, out);
	}
	
	@Test
	public void canParseAndTranslateMultilineAttributes() {
		String jsx = "return <a \n\tx=\"abc\"\n\ty=\"def\"\n\tz=\"stuff\" />";
		ECleverParser parser = new ECleverParser(jsx);
		ReturnStatement stmt = parser.<ReturnStatement>doParse(parser::return_statement, true);
		assertNotNull(stmt.getExpression());
		CodeWriter writer = new CodeWriter(Dialect.E, Context.newGlobalContext());
		stmt.toDialect(writer);
		String out = writer.toString();
		assertEquals(jsx, out);
	}
}
