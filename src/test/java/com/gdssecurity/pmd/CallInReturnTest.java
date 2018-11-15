package com.gdssecurity.pmd;

import org.junit.Assert;
import org.junit.Test;

public class CallInReturnTest {

	public CallInReturnTest() {
		super();
	}
	@Test
	public void testCallInReturn() throws Exception {
		Assert.assertEquals(1,
				PMDRunner.run("src/test/java/resources/others/CallInReturnExample.java", PMDRunner.RULESET_SQL_INJECTION));
	}
	@Test
	public void testCallInReturnWithParenthesis() throws Exception {
		Assert.assertEquals(1,
				PMDRunner.run("src/test/java/resources/others/CallInReturnWithParenthesis.java", PMDRunner.RULESET_SQL_INJECTION));
	}


}
