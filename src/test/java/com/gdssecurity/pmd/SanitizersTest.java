package com.gdssecurity.pmd;

import org.junit.Assert;
import org.junit.Test;

public class SanitizersTest {

	public SanitizersTest() {
		super();
	}

	@Test
	public void testSanitizers() throws Exception {
		int violations = 
				PMDRunner.run(
						"src/test/java/resources/cwe931xss/XSSSanitizers.java", 
						PMDRunner.RULESET_XSS
		);
		Assert.assertEquals(1, violations);
	}
	
	@Test
	public void testSanitizersESAPI() throws Exception {
		int violations = 
				PMDRunner.run(
						"src/test/java/resources/cwe931xss/XSSSanitizersESAPI.java", 
						PMDRunner.RULESET_XSS
		);
		Assert.assertEquals(0, violations);
	}
	@Test
	public void testSanitizersOWASPEncoder() throws Exception {
		int violations = 
				PMDRunner.run(
						"src/test/java/resources/cwe931xss/XSSSanitizersOWASPEncoder.java", 
						PMDRunner.RULESET_XSS
		);
		Assert.assertEquals(0, violations);
	}
	
}
