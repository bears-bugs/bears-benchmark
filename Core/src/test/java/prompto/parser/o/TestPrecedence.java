package prompto.parser.o;

import static org.junit.Assert.assertEquals;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.expression.IExpression;
import prompto.parser.OCleverParser;
import prompto.parser.OPromptoBuilder;
import prompto.runtime.Context;
import prompto.runtime.utils.Out;
import prompto.value.Integer;



public class TestPrecedence extends BaseOParserTest {

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
		IExpression exp = parseExpression("1-2-3-4");
		Context context = Context.newGlobalContext();
		Object value = exp.interpret(context);
		assertEquals(-8L,((Integer)value).longValue());
	}
	
	@Test
	public void test2Plus3Minuses() throws Exception {
		IExpression exp = parseExpression("1+2-3+4-5-6");
		Context context = Context.newGlobalContext();
		Object value = exp.interpret(context);
		assertEquals(-7L,((Integer)value).longValue());
	}
	
	@Test
	public void test1Star1Plus() throws Exception {
		IExpression exp = parseExpression("1*2+3");
		Context context = Context.newGlobalContext();
		Object value = exp.interpret(context);
		assertEquals(5L,((Integer)value).longValue());
	}

	IExpression parseExpression(String exp) {
		OCleverParser parser = new OCleverParser(exp);
		ParseTree tree = parser.expression();
		OPromptoBuilder builder = new OPromptoBuilder(parser);
		ParseTreeWalker walker = new ParseTreeWalker();
		walker.walk(builder, tree);
		return builder.<IExpression>getNodeValue(tree);
	}
	

}
