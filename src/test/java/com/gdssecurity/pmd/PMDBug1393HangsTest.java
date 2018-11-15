package com.gdssecurity.pmd;

import org.junit.Assert;
import org.junit.Test;

public class PMDBug1393HangsTest {

	public PMDBug1393HangsTest() {
		super();
	}
	
	@Test(timeout=10000)
	public void testTimeoutPMDBug1393() throws Exception{
		Assert.assertEquals(0, PMDRunner.run("src/test/java/resources/others/pmdbug1393/PMDBug1393.java", PMDRunner.RULESET_UNVALIDATED_REDIRECTS));
	}
}
