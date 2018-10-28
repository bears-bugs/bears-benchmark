package prompto.parser.e;

import static org.junit.Assert.*;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.expression.AndExpression;
import prompto.expression.IExpression;
import prompto.parser.ECleverParser;
import prompto.parser.EIndentingLexer;
import prompto.parser.EPromptoBuilder;
import prompto.runtime.Context;
import prompto.runtime.utils.Out;
import prompto.value.Integer;



public class TestPrecedence extends BaseEParserTest {

	@Before
	public void before() {
		Out.init();
	}
	
	@After
	public void after() {
		Out.restore();
	}

	@Test
	public void test3Minuses() throws Exception {
		IExpression exp = parse_expression("1-2-3-4");
		Context context = Context.newGlobalContext();
		Object value = exp.interpret(context);
		assertEquals(-8L,((Integer)value).longValue());
	}
	
	@Test
	public void test2Plus3Minuses() throws Exception {
		IExpression exp = parse_expression("1+2-3+4-5-6");
		Context context = Context.newGlobalContext();
		Object value = exp.interpret(context);
		assertEquals(-7L,((Integer)value).longValue());
	}
	
	@Test
	public void test1Star1Plus() throws Exception {
		IExpression exp = parse_expression("1*2+3");
		Context context = Context.newGlobalContext();
		Object value = exp.interpret(context);
		assertEquals(5L,((Integer)value).longValue());
	}

	@Test
	public void testEqAndIn() throws Exception {
		IExpression exp = parse_expression("module = app and releaseStatus in [PENDING, RELEASED]");
		assertTrue(exp instanceof AndExpression);
	}

	IExpression parse_expression(String code) {
		ECleverParser parser = new ECleverParser(code);
		EIndentingLexer lexer = (EIndentingLexer)parser.getTokenStream().getTokenSource();
		lexer.setAddLF(false);
		ParseTree tree = parser.expression();
		EPromptoBuilder builder = new EPromptoBuilder(parser);
		ParseTreeWalker walker = new ParseTreeWalker();
		walker.walk(builder, tree);
		return builder.<IExpression>getNodeValue(tree);
	}

}
