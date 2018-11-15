package resources.annotations;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;

public class AnnotationExample2 {

	public void test2 (JspWriter out,String selected) throws IOException {
		AnnotationExample example = new AnnotationExample();
		example.test1(out, selected); // warning because test1 is sink
	}
}
