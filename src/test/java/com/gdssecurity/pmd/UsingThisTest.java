package com.gdssecurity.pmd;

import org.junit.Assert;
import org.junit.Test;

public class UsingThisTest {
	
	public UsingThisTest() {
		super();
	}

	@Test
	public void testUsingThis () throws Exception {
		Assert.assertEquals(1, PMDRunner.run("src/test/java/resources/others/UsingThis.java", PMDRunner.RULESET_SQL_INJECTION));
	}
}
