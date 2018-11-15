package resources.cwe931xss;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Encoder;

public class XSSSanitizersESAPI {

	public XSSSanitizersESAPI() {
		super();
	}
	public void executeGood(JspWriter writer, String param) throws IOException {	
		Encoder encoder = ESAPI.encoder();
//		writer.write(ESAPI.encoder().encodeForHTML(param));
		writer.write(encoder.encodeForHTML(param));
	}
}
