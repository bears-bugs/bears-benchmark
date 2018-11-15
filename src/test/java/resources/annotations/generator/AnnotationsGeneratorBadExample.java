package resources.annotations.generator;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;

import com.gdssecurity.pmd.annotations.HTMLGenerator;

public class AnnotationsGeneratorBadExample {

	@HTMLGenerator
	public String generateCodeWithParenthesis(String bad) {
		String badString = "ok-" + bad;
		return (badString);
	}
	@HTMLGenerator
	public String generateCode(String bad) {
		return "ok-" + bad;
	}
	

	
	public void generate (JspWriter out, String bad) throws IOException{
		out.write(generateCode(bad));
	}
}
