package resources.others.bug43;

import java.io.IOException;

//import javax.servlet.jsp.JspWriter;

import org.apache.commons.text.*;

public class SanitizerWildcardImportExample {

	public void function(javax.servlet.jsp.JspWriter writer, String badValue) throws IOException{
		writer.write(StringEscapeUtils.escapeHtml4(badValue));
	}
}
