package com.gdssecurity.pmd;

import org.junit.Assert;
import org.junit.Test;

public class DefaultConfigTest {
	
	public DefaultConfigTest () {
		super();
	}
	
	// Execute default rules over all tests files to ensure no misconfiguration occurs
	@Test(timeout=20000)
	public void defaultConfig() throws Exception {
		Assert.assertTrue(PMDRunner.run("src/test/java/resources", PMDRunner.RULESET_DEFAULT) > 0);
	}

}
