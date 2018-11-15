package com.gdssecurity.pmd;

import org.junit.Assert;
import org.junit.Test;

public class CWE328ReversibleHashTest {

	public CWE328ReversibleHashTest() {
		super();
	}

	@Test
	public void test1() throws Exception {
		Assert.assertEquals(2,
				PMDRunner.run("src/test/java/resources/cwe328reversiblehash/CWE328ReversibleOneWayHashBasic.java", PMDRunner.RULESET_WEAK_CRYPTO));
	}
}
