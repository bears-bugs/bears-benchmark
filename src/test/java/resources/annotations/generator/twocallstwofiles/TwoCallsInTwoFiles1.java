package resources.annotations.generator.twocallstwofiles;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;

import org.apache.commons.text.StringEscapeUtils;

import com.gdssecurity.pmd.annotations.HTMLGenerator;

public class TwoCallsInTwoFiles1 {
	@HTMLGenerator
	public static String generateCode(String bad) {
		return "ok" + StringEscapeUtils.escapeHtml4(bad);		
	}
	
	public void generate(JspWriter out, String bad) throws IOException {
		out.write(generateCode(bad));
	}

}
