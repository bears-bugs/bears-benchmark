package resources.annotations;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;

import com.gdssecurity.pmd.annotations.HTMLSink;

public class AnnotationExampleValidateParamsQualified {
	
	@HTMLSink
	public void test1 (JspWriter out, String selected) throws IOException {
		out.write(selected); // Should not trigger violation because is annotated sink
	}
	
	public void test2 (JspWriter out,String selected) throws IOException {
		this.test1(out, selected); // warning because test1 is sink
	}
}
