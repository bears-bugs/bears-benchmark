package com.gdssecurity.pmd;

import org.junit.Assert;
import org.junit.Test;

import com.gdssecurity.pmd.PMDRunner;

public class SinkFullPackageNameTest {

	
	public SinkFullPackageNameTest () {
		super();
	}

	@Test
	public void testSanitizers() throws Exception {
		int violations = 
				PMDRunner.run(
						"src/test/java/resources/others/SinkFullPackageExample.java", 
						PMDRunner.RULESET_XSS
		);
		Assert.assertEquals(1, violations);
	}
}
