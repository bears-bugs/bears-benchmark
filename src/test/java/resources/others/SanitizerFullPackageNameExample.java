package resources.others;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;

public class SanitizerFullPackageNameExample {

	public void function(JspWriter writer, String badValue) throws IOException{
		writer.write(org.apache.commons.text.StringEscapeUtils.escapeHtml4(badValue));
	}
}
