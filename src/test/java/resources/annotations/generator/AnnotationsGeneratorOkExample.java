package resources.annotations.generator;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;


import com.gdssecurity.pmd.annotations.HTMLGenerator;

public class AnnotationsGeneratorOkExample {

	
	@SuppressWarnings("deprecation")
	@HTMLGenerator
	public String generateCode(String bad) {
		return "ok" + org.apache.commons.lang3.StringEscapeUtils.escapeHtml4(bad);
	}
	
	@HTMLGenerator
	public String generateCodeCommonsText(String bad) {
		return "ok" + org.apache.commons.text.StringEscapeUtils.escapeHtml4(bad);
	}
	
	public void generate (JspWriter out, String bad) throws IOException{
		out.write(generateCode(bad));
		out.write(generateCodeCommonsText(bad));
	}
}
