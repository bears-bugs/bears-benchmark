package resources.annotations.generator;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;

public class AnnotationsGeneratorExample {

	
	public String generateCode(String bad) {
		return "ok" + bad;
	}
	
	public void generate (JspWriter out, String bad) throws IOException{
		out.write(generateCode(bad));
	}
}
