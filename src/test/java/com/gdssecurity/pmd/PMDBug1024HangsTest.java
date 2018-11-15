package com.gdssecurity.pmd;

import org.junit.Assert;
import org.junit.Test;

public class PMDBug1024HangsTest {

	
	public PMDBug1024HangsTest () {
		super();
	}
	
	@Test(timeout=10000)
	public void testTimeoutPMDBug1024 () throws Exception{
		Assert.assertEquals(0, PMDRunner.run("src/test/java/resources/others/pmdbug1024/PMDBug1024.java", PMDRunner.RULESET_UNVALIDATED_REDIRECTS));
	}
}
