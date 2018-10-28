package prompto.parser.m;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import prompto.parser.Dialect;
import prompto.parser.MCleverParser;
import prompto.runtime.Context;
import prompto.statement.ReturnStatement;
import prompto.utils.CodeWriter;

public class TestParserJsx extends BaseMParserTest {

	@Test
	public void canParseAndTranslateMultilineElements() {
		String jsx = "return <a>\n\t<b/>\n\t<b/>\n</a>";
		MCleverParser parser = new MCleverParser(jsx);
		ReturnStatement stmt = parser.<ReturnStatement>doParse(parser::return_statement, true);
		assertNotNull(stmt.getExpression());
		CodeWriter writer = new CodeWriter(Dialect.M, Context.newGlobalContext());
		stmt.toDialect(writer);
		String out = writer.toString();
		assertEquals(jsx, out);
	}
	
	@Test
	public void canParseAndTranslateMultilineAttributes() {
		String jsx = "return <a \n\tx=\"abc\"\n\ty=\"def\"\n\tz=\"stuff\" />";
		MCleverParser parser = new MCleverParser(jsx);
		ReturnStatement stmt = parser.<ReturnStatement>doParse(parser::return_statement, true);
		assertNotNull(stmt.getExpression());
		CodeWriter writer = new CodeWriter(Dialect.M, Context.newGlobalContext());
		stmt.toDialect(writer);
		String out = writer.toString();
		assertEquals(jsx, out);
	}

}
