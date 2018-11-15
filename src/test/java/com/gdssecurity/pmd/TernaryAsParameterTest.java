package com.gdssecurity.pmd;

import org.junit.Assert;
import org.junit.Test;

public class TernaryAsParameterTest {

	public TernaryAsParameterTest() {
		super();
	}
	
	@Test
	public void testTernary () throws Exception {
		Assert.assertEquals(0, PMDRunner.run("src/test/java/resources/others/TernaryAsParameter.java", PMDRunner.RULESET_XSS));
	}
	
	@Test
	public void testTernaryWithParenthesis () throws Exception {
		Assert.assertEquals(0, PMDRunner.run("src/test/java/resources/others/TernaryAsParameterWithParenthesis.java", PMDRunner.RULESET_XSS));
	}
	
	@Test

	public void testTernaryInStringConcatenation () throws Exception {
		Assert.assertEquals(0, PMDRunner.run("src/test/java/resources/others/TernaryInStringConcatenation.java", PMDRunner.RULESET_SQL_INJECTION));
	}
	
	@Test
	public void testTernaryInStringConcatenationMultipleConditions () throws Exception {
		Assert.assertEquals(0, PMDRunner.run("src/test/java/resources/others/TernaryInStringConcatenationMultipleConditions.java", PMDRunner.RULESET_SQL_INJECTION));
	}
}
