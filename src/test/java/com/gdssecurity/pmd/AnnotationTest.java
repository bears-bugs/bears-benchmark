package com.gdssecurity.pmd;

import org.junit.Assert;
import org.junit.Test;

public class AnnotationTest {

	private static final String RULESET_ANNOTATIONS = "rulesets/xss-annotations.xml";
	
	
	public AnnotationTest() {
		super();
	}
	
	@Test
	public void testSinkIsOkStringConcat() throws Exception {
		Assert.assertEquals(0, PMDRunner.run("src/test/java/resources/annotations/AnnotationExampleSinkConcat.java", RULESET_ANNOTATIONS));	
	}
	
	@Test
	public void testSinkIsOk () throws Exception {
		Assert.assertEquals(0, PMDRunner.run("src/test/java/resources/annotations/AnnotationExample.java", RULESET_ANNOTATIONS));	
	}
	@Test
	public void testCallingSinkIsNotOk () throws Exception {
		Assert.assertEquals(1, PMDRunner.run("src/test/java/resources/annotations/AnnotationExample2.java", RULESET_ANNOTATIONS));	
	}
	
	@Test
	public void testCallinkSinkValidateParams() throws Exception {
		Assert.assertEquals(1, PMDRunner.run("src/test/java/resources/annotations/AnnotationExampleValidateParams.java", RULESET_ANNOTATIONS));
	}
	
	@Test
	public void testCallinkSinkValidateParamsQualified() throws Exception {
		Assert.assertEquals(1, PMDRunner.run("src/test/java/resources/annotations/AnnotationExampleValidateParamsQualified.java", RULESET_ANNOTATIONS));
	}
	
	@Test
	public void testCallinkSinkValidateParamsStatic() throws Exception {
		Assert.assertEquals(1, PMDRunner.run("src/test/java/resources/annotations/AnnotationExampleValidateParamsStatic.java", RULESET_ANNOTATIONS));
	}
	@Test
	public void testCallinkSinkValidateParamsStaticQualified() throws Exception {
		Assert.assertEquals(1, PMDRunner.run("src/test/java/resources/annotations/AnnotationExampleValidateParamsStaticQualified.java", RULESET_ANNOTATIONS));
	}	
	@Test
	public void testCallinkSinkValidateParamsStaticOtherClass() throws Exception {
		Assert.assertEquals(1, PMDRunner.run("src/test/java/resources/annotations/AnnotationExampleValidateParamsStaticOtherClass.java", RULESET_ANNOTATIONS));
	}
	
	
}
