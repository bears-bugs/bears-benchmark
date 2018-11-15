package resources.annotations;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;

public class AnnotationExampleValidateParamsStaticOtherClass {
	
	
	public void test2 (JspWriter out,String selected) throws IOException {
		AnnotationExampleValidateParamsStatic.test1(out, selected); // warning because test1 is sink
	}
}
