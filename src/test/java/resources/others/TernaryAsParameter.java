package resources.others;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;

public class TernaryAsParameter {

	
	public void ternaryConditionalOperatorIsOk(JspWriter out, String selected) throws IOException {
		out.print("Y".equals(selected) ? "selected" : ""); // it's ok
	}
}
