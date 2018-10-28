package prompto.parser.e;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prompto.argument.CategoryArgument;
import prompto.argument.IArgument;
import prompto.grammar.Identifier;
import prompto.java.JavaStatement;
import prompto.parser.ECleverParser;
import prompto.parser.EIndentingLexer;
import prompto.parser.EPromptoBuilder;
import prompto.runtime.Context;
import prompto.runtime.utils.Out;
import prompto.type.TextType;
import prompto.value.Text;

public class TestRuntime extends BaseEParserTest {

	@Before
	public void before() {
		Out.init();
	}
	
	@After
	public void after() {
		Out.restore();
	}
	
	@Test
	public void testSystemOutPrint() throws Exception {
		ECleverParser parser = new ECleverParser("System.out.print(value);");
		EIndentingLexer lexer = (EIndentingLexer)parser.getTokenStream().getTokenSource();
		lexer.setAddLF(false);
		ParseTree tree = parser.java_statement();
		EPromptoBuilder builder = new EPromptoBuilder(parser);
		ParseTreeWalker walker = new ParseTreeWalker();
		walker.walk(builder, tree);
		JavaStatement statement =  builder.<JavaStatement>getNodeValue(tree);
		Context context = Context.newGlobalContext();
		IArgument arg = new CategoryArgument(TextType.instance(), new Identifier("value"));
		arg.register(context);
		context.setValue(new Identifier("value"), new Text("test")); // StringLiteral trims enclosing quotes
		Object result = statement.interpret(context, null);
		assertNull(result);
		assertEquals("test", Out.read());
	}

	@Test
	public void testReturn() throws Exception {
		interpretResource("native/return.pec", false);
		assertEquals(System.getProperty("os.name"), Out.read());
	}
	
	@Test
	public void testCompiledReturn() throws Exception {
		executeResource("native/return.pec", false);
		assertEquals(System.getProperty("os.name"), Out.read());
	}

	@Test
	public void testDateTimeTZName() throws Exception {
		interpretResource("builtins/dateTimeTZName.pec", false);
		assertEquals("tzName=UTC", Out.read());
	}

}
