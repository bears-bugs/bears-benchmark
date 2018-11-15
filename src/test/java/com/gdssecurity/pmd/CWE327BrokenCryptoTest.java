package com.gdssecurity.pmd;

import org.junit.Assert;
import org.junit.Test;

public class CWE327BrokenCryptoTest {

	public CWE327BrokenCryptoTest() {
		super();
	}

	@Test
	public void test1() throws Exception {
		Assert.assertEquals(1,
				PMDRunner.run("src/test/java/resources/cwe327brokencrypto/CWE327UseBrokenCryptoBasic.java", PMDRunner.RULESET_WEAK_CRYPTO));
	}
}
