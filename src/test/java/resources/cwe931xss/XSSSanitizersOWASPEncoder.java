package resources.cwe931xss;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;

import org.owasp.encoder.Encode;

public class XSSSanitizersOWASPEncoder {

	public XSSSanitizersOWASPEncoder() {
		super();
	}
	public void executeGood(JspWriter writer, String param) throws IOException {	
		writer.write(Encode.forHtml(param));
	}
}
