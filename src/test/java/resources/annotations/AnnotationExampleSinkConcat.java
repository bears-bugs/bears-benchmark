package resources.annotations;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;

import com.gdssecurity.pmd.annotations.HTMLSink;

public class AnnotationExampleSinkConcat {

	
	@HTMLSink
	public  void test1(JspWriter out, String selected) throws IOException {
		String result = "this is a constant " + selected + " more constants";
		out.write(result); // No warning because is sink
	}
}
