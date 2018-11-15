package com.gdssecurity.pmd;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class GetterForConnectionTest {
	
	public GetterForConnectionTest() {
		super();
	}
	@Test
	@Ignore("not yet fixed")
	public void testGetterForConnection () throws Exception {
		Assert.assertEquals(1, PMDRunner.run("src/test/java/resources/others/bug30/DemoUsingGetterBug30.java", PMDRunner.RULESET_SQL_INJECTION));
	}
}
