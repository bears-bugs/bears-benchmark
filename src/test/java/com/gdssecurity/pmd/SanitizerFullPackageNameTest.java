package com.gdssecurity.pmd;

import org.junit.Assert;
import org.junit.Test;

public class SanitizerFullPackageNameTest {

	
	public SanitizerFullPackageNameTest () {
		super();
	}

	@Test
	public void testSanitizers() throws Exception {
		int violations = 
				PMDRunner.run(
						"src/test/java/resources/others/SanitizerFullPackageNameExample.java", 
						PMDRunner.RULESET_XSS
		);
		Assert.assertEquals(0, violations);
	}
}
