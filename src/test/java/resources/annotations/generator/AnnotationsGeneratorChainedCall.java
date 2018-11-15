package resources.annotations.generator;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;

import org.apache.commons.text.StringEscapeUtils;

import com.gdssecurity.pmd.annotations.HTMLGenerator;

public class AnnotationsGeneratorChainedCall {

		
		
	@HTMLGenerator
	public String generateCode(String bad) {
		return "ok" + StringEscapeUtils.escapeHtml4(bad);
	}

	public void generate(JspWriter out, String bad) throws IOException {
		out.write(generateCode(bad));
	}

	public void generate2(JspWriter out, String bad) throws IOException {
		String goodCode = new AnnotationsGeneratorChainedCall().generateCode(bad);
		out.write(goodCode);
	}
	public void generate3(JspWriter out, String bad) throws IOException {
		String goodCode = new AnnotationsGeneratorExample().generateCode(bad);
		out.write(goodCode);
	}	

}
