package com.gdssecurity.pmd;

import org.junit.Assert;
import org.junit.Test;

public class AnnotationsGeneratorTest {

	private static final String RULESET_ANNOTATIONS = "rulesets/xss-annotations.xml";
	
	public AnnotationsGeneratorTest() {
		super();
	}
	
	@Test
	public void testNoGenerator () throws Exception {
		Assert.assertEquals(1, PMDRunner.run("src/test/java/resources/annotations/generator/AnnotationsGeneratorExample.java", RULESET_ANNOTATIONS));	
	}
	@Test
	public void testGeneratorOk () throws Exception {
		Assert.assertEquals(0, PMDRunner.run("src/test/java/resources/annotations/generator/AnnotationsGeneratorOkExample.java", RULESET_ANNOTATIONS));	
	}
	@Test
	public void testGeneratorBad () throws Exception {
		Assert.assertEquals(2, PMDRunner.run("src/test/java/resources/annotations/generator/AnnotationsGeneratorBadExample.java", RULESET_ANNOTATIONS));	
	}
	
	@Test
	public void testGeneratorStatic() throws Exception {
		Assert.assertEquals(1, PMDRunner.run("src/test/java/resources/annotations/generator/AnnotationsGeneratorStatic.java", RULESET_ANNOTATIONS));
	}
	@Test
	public void testGeneratorStaticCaller() throws Exception {
		Assert.assertEquals(0, PMDRunner.run("src/test/java/resources/annotations/generator/AnnotationsGeneratorStaticCaller.java", RULESET_ANNOTATIONS));
	}
	
	@Test
	public void testGeneratorPrivateCallOk() throws Exception {
		Assert.assertEquals(0, PMDRunner.run("src/test/java/resources/annotations/generator/AnnotationsGeneratorPrivateCallOk.java", RULESET_ANNOTATIONS));
	}
	@Test
	public void testGeneratorPrivateCallBad() throws Exception {
		Assert.assertEquals(1, PMDRunner.run("src/test/java/resources/annotations/generator/AnnotationsGeneratorPrivateCallBad.java", RULESET_ANNOTATIONS));
	}
	@Test
	public void testGeneratorTwoCallsInOneFile() throws Exception {
		Assert.assertEquals(0, PMDRunner.run("src/test/java/resources/annotations/generator/twocallsonefile/TwoCallsInOneFile.java", RULESET_ANNOTATIONS));
	}
	
	@Test
	public void testGeneratorTwoCallsInTwoFiles() throws Exception {
		Assert.assertEquals(0, PMDRunner.run("src/test/java/resources/annotations/generator/twocallstwofiles", RULESET_ANNOTATIONS));
	}
	
	@Test
	public void testGeneratorStringBuilder() throws Exception {
		Assert.assertEquals(2, PMDRunner.run("src/test/java/resources/annotations/generator/AnnotationsGeneratorStringBuilder.java", RULESET_ANNOTATIONS));
	}
	
	
	@Test
	public void testGeneratorChainedCall() throws Exception {
		Assert.assertEquals(0, PMDRunner.run("src/test/java/resources/annotations/generator/AnnotationsGeneratorChainedCall.java", RULESET_ANNOTATIONS));
	}
}
