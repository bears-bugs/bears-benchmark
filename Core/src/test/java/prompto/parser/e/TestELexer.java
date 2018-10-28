package prompto.parser.e;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import prompto.parser.ELexer;



public class TestELexer extends BaseELexerTest {
	
	@Test
	public void testTokens() throws Exception {
		String actual = parseTokenNamesFromString("{\n");
		String expected = tokenNamesAsString(new int[] { ELexer.LCURL, ELexer.LF});
		assertEquals(expected,actual);
		
	}
	

	@Test
	public void testIntegerAttribute() {
		String actual = parseTokenNamesFromString("define id as: Integer attribute");
		String expected = tokenNamesAsString(new int[] { ELexer.DEFINE, ELexer.VARIABLE_IDENTIFIER, 
				ELexer.AS, ELexer.COLON, ELexer.INTEGER, ELexer.ATTRIBUTE, ELexer.LF });
		assertEquals(expected,actual);
	}

	@Test
	public void testStringAttribute() {
		String actual = parseTokenNamesFromString("define name as: Text attribute");
		String expected = tokenNamesAsString(new int[] { ELexer.DEFINE, ELexer.VARIABLE_IDENTIFIER, 
				ELexer.AS, ELexer.COLON, ELexer.TEXT, ELexer.ATTRIBUTE, ELexer.LF });
		assertEquals(expected,actual);
	}
	
	@Test
	public void testPersonCategory() {
		String actual = parseTokenNamesFromString("define Person as: category with attributes: id, name");
		String expected = tokenNamesAsString(new int[] { ELexer.DEFINE, ELexer.TYPE_IDENTIFIER, 
				ELexer.AS, ELexer.COLON, ELexer.CATEGORY, ELexer.WITH, ELexer.ATTRIBUTES,
				ELexer.COLON, ELexer.VARIABLE_IDENTIFIER, ELexer.COMMA, ELexer.VARIABLE_IDENTIFIER, ELexer.LF });
		assertEquals(expected,actual);
	}
	
	@Test
	public void testEmployeeCategoryExtendsPerson() {
		String actual = parseTokenNamesFromString("define Employee as: Person with attribute: company");
		String expected = tokenNamesAsString(new int[] { ELexer.DEFINE, ELexer.TYPE_IDENTIFIER, 
				ELexer.AS, ELexer.COLON, ELexer.TYPE_IDENTIFIER, ELexer.WITH, ELexer.ATTRIBUTE, 
				ELexer.COLON, ELexer.VARIABLE_IDENTIFIER, ELexer.LF });
		assertEquals(expected,actual);
	}

	@Test
	public void testEmptyLine() {
		String actual = parseTokenNamesFromString("a\n\t\nb");
		String expected = tokenNamesAsString(new int[] { 
				ELexer.VARIABLE_IDENTIFIER, ELexer.LF, ELexer.LF, ELexer.VARIABLE_IDENTIFIER, ELexer.LF });
		assertEquals(expected,actual);
	}

	@Test
	public void test1Indent() {
		String actual = parseTokenNamesFromString("a\n\tb");
		String expected = tokenNamesAsString(new int[] { ELexer.VARIABLE_IDENTIFIER, ELexer.LF, 
				ELexer.INDENT, ELexer.VARIABLE_IDENTIFIER, ELexer.DEDENT, ELexer.LF });
		assertEquals(expected,actual);
	}

	@Test
	public void test2Indents() {
		String actual = parseTokenNamesFromString("a\n\tb\n\t\tc\n\td");
		String expected = tokenNamesAsString(new int[] { ELexer.VARIABLE_IDENTIFIER, ELexer.LF, 
				ELexer.INDENT, ELexer.VARIABLE_IDENTIFIER, ELexer.LF,
				ELexer.INDENT, ELexer.VARIABLE_IDENTIFIER, ELexer.DEDENT, 
				ELexer.LF, ELexer.VARIABLE_IDENTIFIER, ELexer.DEDENT, 
				ELexer.LF });
		assertEquals(expected,actual);
	}
	
	@Test
	public void testCharLiteral() {
		String actual = parseTokenNamesFromString("'a'");
		String expected = tokenNamesAsString(new int[] { ELexer.CHAR_LITERAL, ELexer.LF });
		assertEquals(expected,actual);
	}
	
	@Test
	public void testDateLiteral() {
		String actual = parseTokenNamesFromString("'2012-10-10'");
		String expected = tokenNamesAsString(new int[] { ELexer.DATE_LITERAL, ELexer.LF });
		assertEquals(expected,actual);
	}
	

	@Test
	public void testTimeLiteral() {
		String actual = parseTokenNamesFromString("'10:10:10'");
		String expected = tokenNamesAsString(new int[] { ELexer.TIME_LITERAL, ELexer.LF });
		assertEquals(expected,actual);
	}
	
	@Test
	public void testDateTimeLiteral() {
		String actual = parseTokenNamesFromString("'2012-10-10T10:10:10'");
		String expected = tokenNamesAsString(new int[] { ELexer.DATETIME_LITERAL, ELexer.LF });
		assertEquals(expected,actual);
	}
	
	@Test
	public void testPeriodLiteral() {
		String actual = parseTokenNamesFromString("'P122Y'");
		String expected = tokenNamesAsString(new int[] { ELexer.PERIOD_LITERAL, ELexer.LF });
		assertEquals(expected,actual);
	}
	
	@Test
	public void testRangeLiteral() {
		String actual = parseTokenNamesFromString("1..3");
		String expected = tokenNamesAsString(new int[] { ELexer.INTEGER_LITERAL, ELexer.RANGE, 
				ELexer.INTEGER_LITERAL, ELexer.LF });
		assertEquals(expected,actual);
	}
	
	@Test
	public void testEnumIdentifier() {
		String actual = parseTokenNamesFromString("ENTITY_1");
		String expected = tokenNamesAsString(new int[] { ELexer.SYMBOL_IDENTIFIER, ELexer.LF });
		assertEquals(expected,actual);
	}
	
	@Test
	public void testMethodCallWith() {
		String actual = parseTokenNamesFromString("print with \"person\" + p.name as value");
		String expected = tokenNamesAsString(new int[] { ELexer.VARIABLE_IDENTIFIER, ELexer.WITH, 
				ELexer.TEXT_LITERAL, ELexer.PLUS, ELexer.VARIABLE_IDENTIFIER, ELexer.DOT,
				ELexer.VARIABLE_IDENTIFIER, ELexer.AS, ELexer.VARIABLE_IDENTIFIER, ELexer.LF });
		assertEquals(expected,actual);
	}

	@Test
	public void testUUID() {
		String actual = parseTokenNamesFromString("'11223344-AABB-CCDD-EEFF-112233445566'");
		String expected = tokenNamesAsString(new int[] { ELexer.UUID_LITERAL, ELexer.LF });
		assertEquals(expected,actual);
	}

	
	
}
