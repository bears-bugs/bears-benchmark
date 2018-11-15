package com.gdssecurity.pmd;

import org.junit.Assert;
import org.junit.Test;

public class NonASCIIJavaTest {
	
	public NonASCIIJavaTest () {
		super();
	}
	@Test
	public void testStringManipulation () throws Exception {
		Assert.assertEquals(2, PMDRunner.run("src/test/java/resources/noascii/NonASCIIJavaExample.java", PMDRunner.RULESET_SQL_INJECTION));
	}

}
