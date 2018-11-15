package resources.others;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;

public class TernaryAsParameterWithParenthesis {
	public void ternaryConditionalOperatorIsOk(JspWriter out, String selected) throws IOException {
		out.print("Y".equals(selected) ? "selected" : ""); // it's ok
		out.print(("Y".equals(selected) ? "selected" : "")); // it's ok
		out.print(("Y".equals(selected)) ? "selected" : ""); // it's ok
		out.print((("Y".equals(selected)) ? "selected" : "")); // it's ok
	}
}
