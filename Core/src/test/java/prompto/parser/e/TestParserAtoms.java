package prompto.parser.e;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.tz.ZoneInfoProvider;
import org.junit.Test;

import prompto.argument.CategoryArgument;
import prompto.argument.ExtendedArgument;
import prompto.argument.IArgument;
import prompto.argument.ITypedArgument;
import prompto.declaration.AttributeDeclaration;
import prompto.declaration.CategoryDeclaration;
import prompto.declaration.ConcreteMethodDeclaration;
import prompto.declaration.IDeclaration;
import prompto.declaration.NativeMethodDeclaration;
import prompto.expression.ConstructorExpression;
import prompto.expression.IExpression;
import prompto.expression.NativeSymbol;
import prompto.expression.PlusExpression;
import prompto.expression.UnresolvedIdentifier;
import prompto.expression.UnresolvedSelector;
import prompto.grammar.ArgumentAssignment;
import prompto.grammar.ArgumentAssignmentList;
import prompto.grammar.ArgumentList;
import prompto.grammar.Identifier;
import prompto.instance.IAssignableInstance;
import prompto.intrinsic.PromptoDate;
import prompto.intrinsic.PromptoDateTime;
import prompto.intrinsic.PromptoTime;
import prompto.literal.BooleanLiteral;
import prompto.literal.DateLiteral;
import prompto.literal.DateTimeLiteral;
import prompto.literal.DecimalLiteral;
import prompto.literal.DictLiteral;
import prompto.literal.HexaLiteral;
import prompto.literal.IntegerLiteral;
import prompto.literal.ListLiteral;
import prompto.literal.PeriodLiteral;
import prompto.literal.RangeLiteral;
import prompto.literal.TextLiteral;
import prompto.literal.TimeLiteral;
import prompto.literal.TupleLiteral;
import prompto.literal.VersionLiteral;
import prompto.parser.Dialect;
import prompto.parser.ECleverParser;
import prompto.parser.EIndentingLexer;
import prompto.parser.EPromptoBuilder;
import prompto.runtime.Context;
import prompto.statement.AssignInstanceStatement;
import prompto.statement.IStatement;
import prompto.statement.NativeCall;
import prompto.statement.UnresolvedCall;
import prompto.type.CategoryType;
import prompto.type.ListType;
import prompto.utils.CodeWriter;
import prompto.utils.ExpressionList;
import prompto.utils.IdentifierList;


public class TestParserAtoms {

	@Test
	public void testInteger() throws Exception {
		String statement = "1";
		ETestParser parser = new ETestParser(statement, false);
		IntegerLiteral il = parser.parse_atomic_literal();
		assertNotNull(il);
		assertEquals(1,il.getValue().longValue());
	}

	@Test
	public void testLiteral() throws Exception {
		String statement = "1";
		ETestParser parser = new ETestParser(statement, false);
		IExpression sl = parser.parse_literal_expression();
		assertTrue(sl instanceof IntegerLiteral);
	}

	@Test
	public void testExpression() throws Exception {
		String statement = "1";
		ETestParser parser = new ETestParser(statement, false);
		IExpression sl = parser.parse_expression();
		assertTrue(sl instanceof IntegerLiteral);
	}

	@Test
	public void testEmptyTuple() throws Exception {
		String statement = "()";
		ETestParser parser = new ETestParser(statement, false);
		IExpression tl = parser.parse_literal_expression();
		assertTrue(tl instanceof TupleLiteral);
	}
	
	@Test
	public void testSimpleTuple() throws Exception {
		String statement = "(1,)";
		ETestParser parser = new ETestParser(statement, false);
		IExpression tl = parser.parse_literal_expression();
		assertTrue(tl instanceof TupleLiteral);
	}
	
	@Test
	public void testComplexTuple() throws Exception {
		String statement = "(1,\"John\",'12:12:12')";
		ETestParser parser = new ETestParser(statement, false);
		TupleLiteral literal = parser.parse_tuple_literal();
		assertTrue(literal instanceof TupleLiteral);
		ExpressionList list = ((TupleLiteral)literal).getExpressions();
		assertEquals("1",list.get(0).toString());
		assertEquals("\"John\"",list.get(1).toString());
		assertEquals("'12:12:12'",list.get(2).toString());
		assertEquals( "(1, \"John\", '12:12:12')", literal.toString());
	}
	
	@Test
	public void testRange() throws Exception {
		String statement = "[1..100]";
		ETestParser parser = new ETestParser(statement, false);
		RangeLiteral rl = parser.parse_range_literal();
		assertNotNull(rl);
		assertEquals("1",rl.getFirst().toString());
		assertEquals("100",rl.getLast().toString());
		assertEquals("[1..100]",rl.toString());
	}

	@Test
	public void testAttribute() throws Exception {
		String statement = "define id as Integer attribute\n";
		ETestParser parser = new ETestParser(statement, true);
		AttributeDeclaration ad = parser.parse_attribute_declaration();
		assertNotNull(ad);
		assertEquals("id",ad.getId().toString());
		assertEquals("Integer",ad.getType().getTypeName());
	}

	@Test
	public void testArrayAttribute() throws Exception {
		String statement = "define id as Integer[] attribute\n";
		ETestParser parser = new ETestParser(statement, true);
		AttributeDeclaration ad = parser.parse_attribute_declaration();
		assertNotNull(ad);
		assertEquals("id",ad.getId().toString());
		assertEquals("Integer[]",ad.getType().getTypeName());
	}

	@Test
	public void testCategory1Attribute() throws Exception {
		String statement = "define Person as category with attribute id\n";
		ETestParser parser = new ETestParser(statement, true);
		CategoryDeclaration cd = parser.parse_category_declaration();
		assertNotNull(cd);
		assertEquals("Person",cd.getId().toString());
		assertNull(cd.getDerivedFrom());
		assertNotNull(cd.getAttributes());
		assertTrue(cd.getAttributes().contains(new Identifier("id")));
	}

	@Test
	public void testCategory2Attributes() throws Exception {
		String statement = "define Person as category with attributes id, name";
		ETestParser parser = new ETestParser(statement, false);
		CategoryDeclaration cd = parser.parse_category_declaration();
		assertNotNull(cd);
		assertEquals("Person",cd.getId().toString());
		assertNull(cd.getDerivedFrom());
		assertNotNull(cd.getAttributes());
		assertTrue(cd.getAttributes().contains(new Identifier("id")));
		assertTrue(cd.getAttributes().contains(new Identifier("name")));
	}
	
	@Test
	public void testCategory1Derived1Attribute() throws Exception {
		String statement = "define Employee as Person with attribute company";
		ETestParser parser = new ETestParser(statement, false);
		CategoryDeclaration cd = parser.parse_category_declaration();
		assertNotNull(cd);
		assertEquals("Employee",cd.getId().toString());
		assertNotNull(cd.getDerivedFrom());
		assertTrue(cd.getDerivedFrom().contains(new Identifier("Person")));
		assertNotNull(cd.getAttributes());
		assertTrue(cd.getAttributes().contains(new Identifier("company")));
	}

	@Test
	public void testCategory2DerivedNoAttribute() throws Exception {
		String statement = "define Entrepreneur as Person and Company\n";
		ETestParser parser = new ETestParser(statement, true);
		CategoryDeclaration cd = parser.parse_category_declaration();
		assertNotNull(cd);
		assertEquals("Entrepreneur",cd.getId().toString());
		assertNotNull(cd.getDerivedFrom());
		assertTrue(cd.getDerivedFrom().contains(new Identifier("Person")));
		assertTrue(cd.getDerivedFrom().contains(new Identifier("Company")));
		assertNull(cd.getAttributes());
	}
	
	@Test
	public void testMemberExpression() throws Exception {
		String statement = "p.name";
		ETestParser parser = new ETestParser(statement, false);
		IExpression e = parser.parse_instance_expression();
		assertTrue(e instanceof UnresolvedSelector);
		UnresolvedSelector me = (UnresolvedSelector)e;
		assertEquals("name",me.getName());
		assertTrue(me.getParent() instanceof UnresolvedIdentifier);
		UnresolvedIdentifier uie = (UnresolvedIdentifier)me.getParent();
		assertEquals("p",uie.getName().toString());
	}

	@Test
	public void testArgument() throws Exception {
		String statement = "Person p";
		ETestParser parser = new ETestParser(statement, false);
		ITypedArgument a = parser.parse_typed_argument();
		assertNotNull(a);
		assertEquals("Person",a.getType().getTypeName());
		assertEquals("p",a.getId().toString());
	}

	@Test
	public void testList1Argument() throws Exception {
		String statement = "Person p";
		ETestParser parser = new ETestParser(statement, false);
		ArgumentList l = parser.parse_argument_list();
		assertNotNull(l);
		assertEquals(1,l.size());
	}

	@Test
	public void testList2ArgumentsComma() throws Exception {
		String statement = "Person p, Employee e";
		ETestParser parser = new ETestParser(statement, false);
		ArgumentList l = parser.parse_argument_list();
		assertNotNull(l);
		assertEquals(2,l.size());
	}

	@Test
	public void testList2ArgumentsAnd() throws Exception {
		String statement = "Person p and Employee e";
		ETestParser parser = new ETestParser(statement, false);
		ArgumentList l = parser.parse_argument_list();
		assertNotNull(l);
		assertEquals(2,l.size());
	}

	@Test
	public void testMethodCallExpression() throws Exception {
		String statement = "print \"person\" + p.name";
		ETestParser parser = new ETestParser(statement, false);
		UnresolvedCall ac = parser.parse_method_call();
		assertNotNull(ac);
	}

	@Test
	public void testSimpleArgumentAssignment() throws Exception {
		String statement = "p.name as value";
		ETestParser parser = new ETestParser(statement, false);
		ArgumentAssignment as = parser.parse_argument_assignment();
		assertEquals("value",as.getArgumentId().toString());
		IExpression exp = as.getExpression();
		assertNotNull(exp);
		CodeWriter writer = new CodeWriter(Dialect.E, Context.newGlobalContext());
		as.toDialect(writer);
		assertEquals("p.name as value", writer.toString());		
	}

	@Test
	public void testComplexArgumentAssignment() throws Exception {
		String statement = "\"person\" + p.name as value";
		ETestParser parser = new ETestParser(statement, false);
		ArgumentAssignment as = parser.parse_argument_assignment();
		assertEquals("value",as.getArgumentId().toString());
		IExpression exp = as.getExpression();
		assertTrue(exp instanceof PlusExpression);
		CodeWriter writer = new CodeWriter(Dialect.E, Context.newGlobalContext());
		as.toDialect(writer);
		assertEquals("\"person\" + p.name as value", writer.toString());		
	}

	@Test
	public void testArgumentAssignmentList1Arg() throws Exception {
		String statement = "with \"person\" + p.name as value";
		ETestParser parser = new ETestParser(statement, false);
		ArgumentAssignmentList ls = parser.parse_argument_assignment_list();
		ArgumentAssignment as = ls.get(0);
		assertEquals("value",as.getArgumentId().toString());
		IExpression exp = as.getExpression();
		assertTrue(exp instanceof PlusExpression);
		CodeWriter writer = new CodeWriter(Dialect.E, Context.newGlobalContext());
		as.toDialect(writer);
		assertEquals("\"person\" + p.name as value",writer.toString());
		
	}

	@Test
	public void testMethodCallWith() throws Exception {
		String statement = "print with \"person\" + p.name as value";
		ETestParser parser = new ETestParser(statement, false);
		UnresolvedCall mc = parser.parse_method_call();
		assertNotNull(mc);
		CodeWriter writer = new CodeWriter(Dialect.E, Context.newGlobalContext());
		mc.getCaller().toDialect(writer);
		assertEquals("print",writer.toString());
		assertNotNull(mc.getAssignments());
		ArgumentAssignment as = mc.getAssignments().get(0);
		assertEquals("value",as.getArgumentId().toString());
		IExpression exp = as.getExpression();
		assertTrue(exp instanceof PlusExpression);
		writer = new CodeWriter(Dialect.E, Context.newGlobalContext());
		mc.toDialect(writer);
		assertEquals("print with \"person\" + p.name as value",writer.toString());
		
	}

	@Test
	public void testMethod1Parameter1Statement() throws Exception {
		String statement = "define printName as method receiving Person p doing:\n"
				+ "\tprint with \"person\" + p.name as value\n";
		ETestParser parser = new ETestParser(statement, true);
		ConcreteMethodDeclaration ad = parser.parse_concrete_method_declaration();
		assertNotNull(ad);
		assertEquals("printName",ad.getId().toString());
		assertNotNull(ad.getArguments());
		assertTrue(ad.getArguments().contains(new CategoryArgument(
				new CategoryType(new Identifier("Person")),
				new Identifier("p"))));
		assertNotNull(ad.getStatements());
		CodeWriter writer = new CodeWriter(Dialect.E, Context.newGlobalContext());
		ad.getStatements().getFirst().toDialect(writer);
		assertEquals("print with \"person\" + p.name as value", writer.toString());	
	}
	
	@Test
	public void testMethod1Extended1Statement() throws Exception {
		String statement = "define printName as method receiving Object o with attribute name doing:\n"
				+ "\tprint with \"object\" + o.name as value\n";
		ETestParser parser = new ETestParser(statement, true);
		ConcreteMethodDeclaration ad = parser.parse_concrete_method_declaration();
		assertNotNull(ad);
		assertEquals("printName",ad.getId().toString());
		assertNotNull(ad.getArguments());
		IArgument expected = new ExtendedArgument(
								new CategoryType(new Identifier("Object")),
								new Identifier("o"), 
								new IdentifierList(new Identifier("name")));
		assertTrue(ad.getArguments().contains(expected));
		assertNotNull(ad.getStatements());
		CodeWriter writer = new CodeWriter(Dialect.E, Context.newGlobalContext());
		ad.getStatements().getFirst().toDialect(writer);
		assertEquals("print with \"object\" + o.name as value", writer.toString());
	}
	
	@Test
	public void testMethod1Array1Statement() throws Exception {
		String statement = "define printName as method receiving Option[] options doing:\n"
				+ "\tprint with \"array\" + args as value\n";
		ETestParser parser = new ETestParser(statement, true);
		ConcreteMethodDeclaration ad = parser.parse_concrete_method_declaration();
		assertNotNull(ad);
		assertEquals("printName",ad.getId().toString());
		assertNotNull(ad.getArguments());
		IArgument expected = new CategoryArgument(new ListType(
				new CategoryType(new Identifier("Option"))),
				new Identifier("options"));
		assertTrue(ad.getArguments().contains(expected)); 
		assertNotNull(ad.getStatements());
		CodeWriter writer = new CodeWriter(Dialect.E, Context.newGlobalContext());
		ad.getStatements().getFirst().toDialect(writer);
		assertEquals("print with \"array\" + args as value", writer.toString());
	}
	
	@Test
	public void testConstructorSynonymsExpression() throws Exception {
		String statement = "Company with id and name";
		ETestParser parser = new ETestParser(statement, false);
		IExpression e = parser.parse_expression();
		assertTrue(e instanceof UnresolvedCall);
	}

	
	@Test
	public void testConstructorSynonyms() throws Exception {
		String statement = "Company with id";
		ETestParser parser = new ETestParser(statement, false);
		ConstructorExpression c = parser.parse_constructor_expression();
		assertNotNull(c);
	}
	
	@Test 
	public void testConstructor1Attribute() throws Exception {
		String statement = "Company with 1 as id";
		ETestParser parser = new ETestParser(statement, false);
		ConstructorExpression c = parser.parse_constructor_expression();
		assertNotNull(c);
	}

	@Test 
	public void testConstructorFrom1Attribute() throws Exception {
		String statement = "Company from entity with 1 as id";
		ETestParser parser = new ETestParser(statement, false);
		ConstructorExpression c = parser.parse_constructor_expression();
		assertNotNull(c);
	}

	@Test 
	public void testConstructor2AttributesComma() throws Exception {
		String statement = "Company with 1 as id, \"IBM\" as name";
		ETestParser parser = new ETestParser(statement, false);
		ConstructorExpression c = parser.parse_constructor_expression();
		assertNotNull(c);
		ArgumentAssignmentList l = c.getAssignments();
		assertNotNull(l);
		assertEquals(2, l.size());
		ArgumentAssignment a = l.get(0);
		assertNotNull(a);
		assertEquals("id",a.getArgumentId().toString());
		IExpression e = a.getExpression();
		assertNotNull(e);
		assertTrue(e instanceof IntegerLiteral);
		a = l.get(1);
		assertNotNull(a);
		assertEquals("name",a.getArgumentId().toString());
		e = a.getExpression();
		assertNotNull(e);
		assertTrue(e instanceof TextLiteral);
	}
	
	@Test 
	public void testConstructor2AttributesAnd() throws Exception {
		String statement = "Company with 1 as id and \"IBM\" as name";
		ETestParser parser = new ETestParser(statement, false);
		ConstructorExpression c = parser.parse_constructor_expression();
		assertNotNull(c);
		ArgumentAssignmentList l = c.getAssignments();
		assertNotNull(l);
		assertEquals(2, l.size());
		ArgumentAssignment a = l.get(0);
		assertNotNull(a);
		assertEquals("id",a.getArgumentId().toString());
		IExpression e = a.getExpression();
		assertNotNull(e);
		assertTrue(e instanceof IntegerLiteral);
		a = l.get(1);
		assertNotNull(a);
		assertEquals("name",a.getArgumentId().toString());
		e = a.getExpression();
		assertNotNull(e);
		assertTrue(e instanceof TextLiteral);
	}

	@Test 
	public void testAssignmentConstructor() throws Exception {
		String statement = "c = Company from x with 1 as id and \"IBM\" as name";
		ETestParser parser = new ETestParser(statement, false);
		AssignInstanceStatement a = parser.parse_assign_instance_statement();
		assertNotNull(a);
		assertTrue(a.getExpression() instanceof ConstructorExpression);
	}
	
	@Test 
	public void testNativeJava() throws Exception {
		String statement = "Java: System.out.println(value);\n";
		ETestParser parser = new ETestParser(statement, true);
		NativeCall call = parser.parse_native_statement();
		assertNotNull(call);
		assertTrue(call instanceof NativeCall);
	}
	
	@Test 
	public void testNativeCSharp() throws Exception {
		String statement = "C#: Console.println(value);\n";
		ETestParser parser = new ETestParser(statement, true);
		NativeCall call = parser.parse_native_statement();
		assertNotNull(call);
		assertTrue(call instanceof NativeCall);
	}
	
	@Test 
	public void testNativeMethod() throws Exception {
		String statement = "define print as native method receiving String value doing:\n"
				+ "\tJava: System.out.println(value);\n"
				+ "\tC#: Console.println(value);\n";
		
		ETestParser parser = new ETestParser(statement, true);
		NativeMethodDeclaration method = parser.parse_native_method_declaration();
		assertNotNull(method);
		assertTrue(method instanceof NativeMethodDeclaration);
	}
	
	@Test
	public void testBooleanLiteral() throws Exception {
		String statement = "true";
		ETestParser parser = new ETestParser(statement, false);
		IExpression literal = parser.parse_literal_expression();
		assertNotNull(literal);
		assertTrue(literal instanceof BooleanLiteral);
		assertEquals("true", literal.toString());
		assertEquals(true, ((BooleanLiteral)literal).getValue().getValue());
		statement = "false";
		parser = new ETestParser(statement, false);
		literal = parser.parse_literal_expression();
		assertNotNull(literal);
		assertTrue(literal instanceof BooleanLiteral);
		assertEquals("false", literal.toString());
		assertEquals(false, ((BooleanLiteral)literal).getValue().getValue());
	}

	@Test
	public void testStringLiteral() throws Exception {
		String statement = "\"hello\"";
		ETestParser parser = new ETestParser(statement, false);
		IExpression literal = parser.parse_literal_expression();
		assertNotNull(literal);
		assertTrue(literal instanceof TextLiteral);
		CodeWriter writer = new CodeWriter(Dialect.E, null);
		literal.toDialect(writer);
		assertEquals("\"hello\"", writer.toString());
		assertEquals("hello", ((TextLiteral)literal).getValue().getStorableData());
	}
	
	@Test
	public void testIntegerLiteral() throws Exception {
		String statement = "1234";
		ETestParser parser = new ETestParser(statement, false);
		IExpression literal = parser.parse_literal_expression();
		assertNotNull(literal);
		assertTrue(literal instanceof IntegerLiteral);
		assertEquals("1234", literal.toString());
		assertEquals(1234L, ((IntegerLiteral)literal).getValue().longValue());
	}
	
	@Test
	public void testVersionLiteral() throws Exception {
		String statement = "'v1.0.0'";
		ETestParser parser = new ETestParser(statement, false);
		IExpression literal = parser.parse_literal_expression();
		assertNotNull(literal);
		assertTrue(literal instanceof VersionLiteral);
		assertEquals("'v1.0.0'", literal.toString());
	}
	
	@Test
	public void testParseHexa() throws Exception {
		assertEquals(0x0A11, HexaLiteral.parseHexa("0x0A11").longValue());
	}
	
	@Test
	public void testHexaLiteral() throws Exception {
		String statement = "0x0A11";
		ETestParser parser = new ETestParser(statement, false);
		IExpression literal = parser.parse_literal_expression();
		assertNotNull(literal);
		assertTrue(literal instanceof HexaLiteral);
		CodeWriter writer = new CodeWriter(Dialect.E, null);
		literal.toDialect(writer);
		assertEquals("0x0A11", literal.toString());
		assertEquals(0x0A11, ((HexaLiteral)literal).getValue().longValue());
	}

	@Test
	public void testDecimalLiteral() throws Exception {
		String statement = "1234.13";
		ETestParser parser = new ETestParser(statement, false);
		IExpression literal = parser.parse_literal_expression();
		assertNotNull(literal);
		assertTrue(literal instanceof DecimalLiteral);
		assertEquals("1234.13", literal.toString());
		assertEquals(1234.13, ((DecimalLiteral)literal).getValue().doubleValue(),0.0000001);
	}
	
	@Test
	public void testEmptyListLiteral() throws Exception {
		String statement = "[]";
		ETestParser parser = new ETestParser(statement, false);
		IExpression literal = parser.parse_literal_expression();
		assertNotNull(literal);
		assertTrue(literal instanceof ListLiteral);
		assertEquals("[]", literal.toString());
		assertEquals(0, ((ListLiteral)literal).getValue().getLength());
	}
	
	@Test
	public void testSimpleListLiteral() throws Exception {
		String statement = "[ john, 123 ]";
		ETestParser parser = new ETestParser(statement, false);
		IExpression literal = parser.parse_literal_expression();
		assertNotNull(literal);
		assertEquals("[john, 123]", literal.toString());
		assertTrue(literal instanceof ListLiteral);
		ExpressionList list = ((ListLiteral)literal).getExpressions();
		assertEquals(2, list.size());
		assertTrue(list.get(0) instanceof UnresolvedIdentifier);
		assertTrue(list.get(1) instanceof IntegerLiteral);
	}
	
	@Test
	public void testEmptyDictLiteral() throws Exception {
		String statement = "<:>";
		ETestParser parser = new ETestParser(statement, false);
		IExpression literal = parser.parse_literal_expression();
		assertNotNull(literal);
		assertTrue(literal instanceof DictLiteral);
		assertEquals("<:>", literal.toString());
	}
	
	@Test
	public void testSimpleDictLiteral() throws Exception {
		String statement = "< \"john\" : 1234, eric : 5678 >";
		ETestParser parser = new ETestParser(statement, false);
		IExpression literal = parser.parse_literal_expression();
		assertNotNull(literal);
		assertTrue(literal instanceof DictLiteral);
		CodeWriter writer = new CodeWriter(Dialect.E, Context.newGlobalContext());
		literal.toDialect(writer);
		assertEquals("<\"john\":1234, eric:5678>", writer.toString()); // TODO DictLiteral
	}
	
	@Test
	public void testSimpleDate() throws Exception {
		String statement = "'2012-10-09'";
		ETestParser parser = new ETestParser(statement, false);
		IExpression literal = parser.parse_literal_expression();
		assertNotNull(literal);
		assertTrue(literal instanceof DateLiteral);
		CodeWriter writer = new CodeWriter(Dialect.E, null);
		literal.toDialect(writer);
		assertEquals("'2012-10-09'", writer.toString());
		assertEquals(new PromptoDate(2012, 10, 9), ((DateLiteral)literal).getValue().getStorableData());
	}

	@Test
	public void testTime() throws Exception {
		String statement = "'15:03:10'";
		ETestParser parser = new ETestParser(statement, false);
		IExpression literal = parser.parse_literal_expression();
		assertNotNull(literal);
		assertTrue(literal instanceof TimeLiteral);
		CodeWriter writer = new CodeWriter(Dialect.E, null);
		literal.toDialect(writer);
		assertEquals("'15:03:10'", writer.toString());
		assertEquals(new PromptoTime(15, 03, 10, 0), ((TimeLiteral)literal).getValue().getStorableData());
	}

	@Test
	public void testDateTime() throws Exception {
		String statement = "'2012-10-09T15:18:17'";
		ETestParser parser = new ETestParser(statement, false);
		IExpression literal = parser.parse_literal_expression();
		assertNotNull(literal);
		assertTrue(literal instanceof DateTimeLiteral);
		CodeWriter writer = new CodeWriter(Dialect.E, null);
		literal.toDialect(writer);
		assertEquals("'2012-10-09T15:18:17'", writer.toString());
		assertEquals(new PromptoDateTime(2012, 10, 9, 15, 18, 17, 0), ((DateTimeLiteral)literal).getValue().getStorableData());
	}
	
	@Test
	public void testDateTimeWithMillis() throws Exception {
		String statement = "'2012-10-09T15:18:17.487'";
		ETestParser parser = new ETestParser(statement, false);
		IExpression literal = parser.parse_literal_expression();
		assertNotNull(literal);
		assertTrue(literal instanceof DateTimeLiteral);
		CodeWriter writer = new CodeWriter(Dialect.E, null);
		literal.toDialect(writer);
		assertEquals("'2012-10-09T15:18:17.487'", writer.toString());
		assertEquals(new PromptoDateTime(2012, 10, 9, 15, 18, 17, 487), ((DateTimeLiteral)literal).getValue().getStorableData());
	}
	
	@Test
	public void testDateTimeWithTZ() throws Exception {
		String statement = "'2012-10-09T15:18:17+02:00'";
		ETestParser parser = new ETestParser(statement, false);
		IExpression literal = parser.parse_literal_expression();
		assertNotNull(literal);
		assertTrue(literal instanceof DateTimeLiteral);
		CodeWriter writer = new CodeWriter(Dialect.E, null);
		literal.toDialect(writer);
		assertEquals("'2012-10-09T15:18:17+02:00'", writer.toString());
		ZoneInfoProvider provider = new ZoneInfoProvider("org/joda/time/tz/data");
		DateTimeZone tz = provider.getZone("Etc/GMT-2");
		DateTime dt = new DateTime(2012, 10, 9, 15, 18, 17, tz);
		PromptoDateTime expected = new PromptoDateTime(dt);
		PromptoDateTime actual = ((DateTimeLiteral)literal).getValue().getStorableData();
		assertTrue(expected.isEqual(actual));
	}
	
	@Test
	public void testPeriod() throws Exception {
		String statement = "'P3Y'";
		ETestParser parser = new ETestParser(statement, false);
		IExpression literal = parser.parse_literal_expression();
		assertNotNull(literal);
		assertTrue(literal instanceof PeriodLiteral);
		CodeWriter writer = new CodeWriter(Dialect.E, null);
		literal.toDialect(writer);
		assertEquals("'P3Y'", writer.toString());
		assertEquals(3,((PeriodLiteral)literal).getValue().getStorableData().getNativeYears());
	}

	@Test
	public void testNativeSymbol() throws Exception {
		String statement = "ENTITY_1 with \"1\" as value\n";
		ETestParser parser = new ETestParser(statement, true);
		IExpression symbol = parser.parse_native_symbol();
		assertNotNull(symbol);
		assertTrue(symbol instanceof NativeSymbol);
		CodeWriter writer = new CodeWriter(Dialect.E, null);
		symbol.toDialect(writer);
		writer.append("\n");
		assertEquals(statement, writer.toString());
	}

	@Test
	public void testExpressionWith() throws Exception {
		String statement = "x = print with \"1\" as value";
		ETestParser parser = new ETestParser(statement, false);
		IStatement stmt = parser.parse_statement();
		assertNotNull(stmt);
		CodeWriter writer = new CodeWriter(Dialect.E, Context.newGlobalContext());
		stmt.toDialect(writer);
		assertEquals(statement, writer.toString());
	}
	
	@Test
	public void testMethodUnresolved() throws Exception {
		String statement = "print";
		ETestParser parser = new ETestParser(statement, false);
		IStatement stmt = parser.parse_statement();
		assertTrue(stmt instanceof UnresolvedCall);
		CodeWriter writer = new CodeWriter(Dialect.E, Context.newGlobalContext());
		stmt.toDialect(writer);
		assertEquals(statement, writer.toString());
	}

	@Test
	public void testMethodExpression() throws Exception {
		String statement = "print a";
		ETestParser parser = new ETestParser(statement, false);
		IStatement stmt = parser.parse_statement();
		assertTrue(stmt instanceof UnresolvedCall);
		CodeWriter writer = new CodeWriter(Dialect.E, Context.newGlobalContext());
		stmt.toDialect(writer);
		assertEquals(statement, writer.toString());
	}

	@Test
	public void testMethodWith() throws Exception {
		String statement = "print \"a\" with \"1\" as value";
		ETestParser parser = new ETestParser(statement, false);
		IStatement stmt = parser.parse_statement();
		assertNotNull(stmt);
		CodeWriter writer = new CodeWriter(Dialect.E, Context.newGlobalContext());
		stmt.toDialect(writer);
		assertEquals(statement, writer.toString());
	}

	@Test
	public void testInstance() throws Exception {
		String statement = "x[y]";
		ETestParser parser = new ETestParser(statement, false);
		IExpression stmt = parser.parse_expression();
		assertNotNull(stmt);
		CodeWriter writer = new CodeWriter(Dialect.E, Context.newGlobalContext());
		stmt.toDialect(writer);
		assertEquals(statement, writer.toString());
	}
	
	@Test
	public void testAssignableInstance() throws Exception {
		String statement = "doc.vals[2]";
		ETestParser parser = new ETestParser(statement, false);
		IAssignableInstance stmt = parser.parse_assignable();
		assertNotNull(stmt);
		CodeWriter writer = new CodeWriter(Dialect.E, null);
		stmt.toDialect(writer, null);
		assertEquals(statement, writer.toString());
	}
	
	@Test
	public void testMainDeclaration() throws Exception {
		String statement = "define start_TestEvents as method receiving Text<:> doing:\n\ta=1";
		ETestParser parser = new ETestParser(statement, false);
		IDeclaration decl = parser.parse_concrete_method_declaration();
		assertNotNull(decl);
	}

	static class ETestParser extends ECleverParser {
		
		public ETestParser(String code, boolean addLF) {
			super(code);
			EIndentingLexer lexer = (EIndentingLexer)getTokenStream().getTokenSource();
			lexer.setAddLF(addLF);
		}

		public IAssignableInstance parse_assignable() {
			ParseTree tree = assignable_instance();
			EPromptoBuilder builder = new EPromptoBuilder(this);
			ParseTreeWalker walker = new ParseTreeWalker();
			walker.walk(builder, tree);
			return builder.<IAssignableInstance>getNodeValue(tree);
		}

		public IntegerLiteral parse_atomic_literal() {
			ParseTree tree = atomic_literal();
			EPromptoBuilder builder = new EPromptoBuilder(this);
			ParseTreeWalker walker = new ParseTreeWalker();
			walker.walk(builder, tree);
			return builder.<IntegerLiteral>getNodeValue(tree);
		}

		public ArgumentAssignmentList parse_argument_assignment_list() {
			ParseTree tree = argument_assignment_list();
			EPromptoBuilder builder = new EPromptoBuilder(this);
			ParseTreeWalker walker = new ParseTreeWalker();
			walker.walk(builder, tree);
			return builder.<ArgumentAssignmentList>getNodeValue(tree);
		}

		public ArgumentAssignment parse_argument_assignment() {
			ParseTree tree = argument_assignment();
			EPromptoBuilder builder = new EPromptoBuilder(this);
			ParseTreeWalker walker = new ParseTreeWalker();
			walker.walk(builder, tree);
			return builder.<ArgumentAssignment>getNodeValue(tree);
		}

		public IExpression parse_instance_expression() {
			ParseTree tree = instance_expression();
			EPromptoBuilder builder = new EPromptoBuilder(this);
			ParseTreeWalker walker = new ParseTreeWalker();
			walker.walk(builder, tree);
			return builder.<IExpression>getNodeValue(tree);
		}

		public RangeLiteral parse_range_literal() {
			ParseTree tree = range_literal();
			EPromptoBuilder builder = new EPromptoBuilder(this);
			ParseTreeWalker walker = new ParseTreeWalker();
			walker.walk(builder, tree);
			return builder.<RangeLiteral>getNodeValue(tree);
		}
		
		public TupleLiteral parse_tuple_literal() {
			ParseTree tree = tuple_literal();
			EPromptoBuilder builder = new EPromptoBuilder(this);
			ParseTreeWalker walker = new ParseTreeWalker();
			walker.walk(builder, tree);
			return builder.<TupleLiteral>getNodeValue(tree);
		}

		public AttributeDeclaration parse_attribute_declaration() {
			ParseTree tree = attribute_declaration();
			EPromptoBuilder builder = new EPromptoBuilder(this);
			ParseTreeWalker walker = new ParseTreeWalker();
			walker.walk(builder, tree);
			return builder.<AttributeDeclaration>getNodeValue(tree);
		}

		public CategoryDeclaration parse_category_declaration() {
			ParseTree tree = category_declaration();
			EPromptoBuilder builder = new EPromptoBuilder(this);
			ParseTreeWalker walker = new ParseTreeWalker();
			walker.walk(builder, tree);
			return builder.<CategoryDeclaration>getNodeValue(tree);
		}

		public ITypedArgument parse_typed_argument() {
			ParseTree tree = typed_argument();
			EPromptoBuilder builder = new EPromptoBuilder(this);
			ParseTreeWalker walker = new ParseTreeWalker();
			walker.walk(builder, tree);
			return builder.<ITypedArgument>getNodeValue(tree);
		}

		public ArgumentList parse_argument_list() {
			ParseTree tree = full_argument_list();
			EPromptoBuilder builder = new EPromptoBuilder(this);
			ParseTreeWalker walker = new ParseTreeWalker();
			walker.walk(builder, tree);
			return builder.<ArgumentList>getNodeValue(tree);
		}

		public UnresolvedCall parse_method_call() {
			ParseTree tree = method_call_statement();
			EPromptoBuilder builder = new EPromptoBuilder(this);
			ParseTreeWalker walker = new ParseTreeWalker();
			walker.walk(builder, tree);
			return builder.<UnresolvedCall>getNodeValue(tree);
		}

		public NativeMethodDeclaration parse_native_method_declaration() {
			ParseTree tree = native_method_declaration();
			EPromptoBuilder builder = new EPromptoBuilder(this);
			ParseTreeWalker walker = new ParseTreeWalker();
			walker.walk(builder, tree);
			return builder.<NativeMethodDeclaration>getNodeValue(tree);
		}

		public ConcreteMethodDeclaration parse_concrete_method_declaration() {
			ParseTree tree = concrete_method_declaration();
			EPromptoBuilder builder = new EPromptoBuilder(this);
			ParseTreeWalker walker = new ParseTreeWalker();
			walker.walk(builder, tree);
			return builder.<ConcreteMethodDeclaration>getNodeValue(tree);
		}

		public ConstructorExpression parse_constructor_expression() {
			ParseTree tree = constructor_expression();
			EPromptoBuilder builder = new EPromptoBuilder(this);
			ParseTreeWalker walker = new ParseTreeWalker();
			walker.walk(builder, tree);
			return builder.<ConstructorExpression>getNodeValue(tree);
		}

		public AssignInstanceStatement parse_assign_instance_statement() {
			ParseTree tree = assign_instance_statement();
			EPromptoBuilder builder = new EPromptoBuilder(this);
			ParseTreeWalker walker = new ParseTreeWalker();
			walker.walk(builder, tree);
			return builder.<AssignInstanceStatement>getNodeValue(tree);
		}

		public NativeCall parse_native_statement() {
			ParseTree tree = native_statement();
			EPromptoBuilder builder = new EPromptoBuilder(this);
			ParseTreeWalker walker = new ParseTreeWalker();
			walker.walk(builder, tree);
			return builder.<NativeCall>getNodeValue(tree);
		}

		public IExpression parse_literal_expression() {
			ParseTree tree = literal_expression();
			EPromptoBuilder builder = new EPromptoBuilder(this);
			ParseTreeWalker walker = new ParseTreeWalker();
			walker.walk(builder, tree);
			return builder.<IExpression>getNodeValue(tree);
		}

		public IExpression parse_native_symbol() {
			ParseTree tree = native_symbol();
			EPromptoBuilder builder = new EPromptoBuilder(this);
			ParseTreeWalker walker = new ParseTreeWalker();
			walker.walk(builder, tree);
			return builder.<IExpression>getNodeValue(tree);
		}

		public IStatement parse_statement() {
			ParseTree tree = statement();
			EPromptoBuilder builder = new EPromptoBuilder(this);
			ParseTreeWalker walker = new ParseTreeWalker();
			walker.walk(builder, tree);
			return builder.<IStatement>getNodeValue(tree);
		}

		public IExpression parse_expression() {
			ParseTree tree = this.expression();
			EPromptoBuilder builder = new EPromptoBuilder(this);
			ParseTreeWalker walker = new ParseTreeWalker();
			walker.walk(builder, tree);
			return builder.<IExpression>getNodeValue(tree);
		}
	}
}
