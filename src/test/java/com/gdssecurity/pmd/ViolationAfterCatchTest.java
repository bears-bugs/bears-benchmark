package com.gdssecurity.pmd;

import org.junit.Assert;
import org.junit.Test;

public class ViolationAfterCatchTest {

	public ViolationAfterCatchTest() {
		super();
	}
	
	@Test
	public void testViolationAfterCatch() throws Exception{
		Assert.assertEquals(1, PMDRunner.run("src/test/java/resources/others/bug28/ViolationAfterCatch.java", PMDRunner.RULESET_XSS));
	}	
}
