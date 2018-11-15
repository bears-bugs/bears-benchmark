package com.gdssecurity.pmd;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class CWE22PathTraversal {

	public CWE22PathTraversal() {
		super();
	}

	@Test
	@Ignore("Not yet implemented")
	public void test1() throws Exception {
		Assert.assertEquals(2, PMDRunner.run("src/test/java/resources/cwe22pathtraversal/PathTraversalExample.java",
				PMDRunner.RULEST_PATH_TRAVERSAL));
	}
}
