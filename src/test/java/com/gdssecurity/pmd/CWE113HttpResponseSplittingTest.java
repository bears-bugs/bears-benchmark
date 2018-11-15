package com.gdssecurity.pmd;

import org.junit.Assert;
import org.junit.Test;

public class CWE113HttpResponseSplittingTest {

	public CWE113HttpResponseSplittingTest() {
		super();
	}

	@Test
	public void testUnvalidatedRedirects() throws Exception {
		Assert.assertEquals(1, PMDRunner.run("src/test/java/resources/cwe113responsesplitting/TestRedirectServlet.java",
				PMDRunner.RULESET_UNVALIDATED_REDIRECTS));
	}
	
	@Test
	public void testHTTPResponseSplitting() throws Exception {
		Assert.assertEquals(1, PMDRunner.run("src/test/java/resources/cwe113responsesplitting/TestRedirectServlet.java",
				PMDRunner.RULESET_HTTP_RESPONSE_SPLITTING));
	}
}
