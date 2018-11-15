package resources.cwe931xss;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;

import org.apache.commons.text.StringEscapeUtils;

public class XSSSanitizers {

	public XSSSanitizers() {
		super();
	}
	public void executeBad(JspWriter writer, String param) throws IOException {	
		writer.write(param);
	}
	public void executeGood(JspWriter writer, String param) throws IOException {	
		writer.write(StringEscapeUtils.escapeHtml4(param));
	}
}
