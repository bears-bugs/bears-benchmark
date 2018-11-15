package resources.annotations.generator.twocallsonefile;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;

import org.apache.commons.text.StringEscapeUtils;

import com.gdssecurity.pmd.annotations.HTMLGenerator;

public class TwoCallsInOneFile {

	@HTMLGenerator
	public String generateCode(String bad) {
		return "ok" + StringEscapeUtils.escapeHtml4(bad);		
	}

	public void generate(JspWriter out, String bad) throws IOException {
		out.write(generateCode(bad));
	}

	public void generate2(JspWriter out, String bad) throws IOException {
		out.write(generateCode(bad));
	}

}
