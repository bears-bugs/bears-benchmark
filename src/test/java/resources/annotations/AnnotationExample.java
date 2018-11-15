package resources.annotations;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;

import com.gdssecurity.pmd.annotations.HTMLSink;

public class AnnotationExample {

	@HTMLSink
	public  void test1(JspWriter out, String selected) throws IOException {
		out.write(selected); // No warning because is sink
	}
	
	
}
