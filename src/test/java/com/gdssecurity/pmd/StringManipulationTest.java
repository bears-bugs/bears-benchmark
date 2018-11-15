package com.gdssecurity.pmd;

import org.junit.Assert;
import org.junit.Test;

public class StringManipulationTest {
	
	public StringManipulationTest() {
		super();
	}
	
	@Test
	public void testStringManipulation () throws Exception {
		Assert.assertEquals(3, PMDRunner.run("src/test/java/resources/others/StringManipulationExample.java", PMDRunner.RULESET_SQL_INJECTION));
	}
}
