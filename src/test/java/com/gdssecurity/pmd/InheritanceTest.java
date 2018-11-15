package com.gdssecurity.pmd;

import org.junit.Assert;
import org.junit.Test;

public class InheritanceTest {
	
	public InheritanceTest () {
		super();
	}

	@Test
	public void test1 () throws Exception {
		Assert.assertEquals(
				2,
				PMDRunner.run("src/test/java/resources/inheritance/", PMDRunner.RULESET_SQL_INJECTION)
		);
	}
}
