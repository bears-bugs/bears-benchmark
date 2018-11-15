package com.gdssecurity.pmd;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class WritingToResponseWithWriter {

	public WritingToResponseWithWriter() {
		super();
	}
	
	@Test
	@Ignore("to be fixed")
	public void test1() throws Exception {
		Assert.assertEquals(1, PMDRunner.run("src/test/java/resources/others/bug31/UsingWriterOnReponseGetOutputStream.java", PMDRunner.RULESET_XSS));
	}
}
