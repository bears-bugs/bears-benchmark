package resources.annotations.generator;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;

import org.apache.commons.text.StringEscapeUtils;

import com.gdssecurity.pmd.annotations.HTMLGenerator;

public class AnnotationsGeneratorPrivateCallBad {

	public void insertHidden(JspWriter out, String id, String value) throws IOException {
		String result = createHidden(id, value);
		out.println(result);
	}
	@HTMLGenerator
	private String createHidden(String id, String value) {
		StringBuilder sb = new StringBuilder();
		sb.append("<input type='hidden'");
		sb.append(" id='").append(id).append("'");
		sb.append(" value='").append(StringEscapeUtils.escapeHtml4(value)).append("'");
		sb.append(" />");
		return sb.toString();		
	}
}
