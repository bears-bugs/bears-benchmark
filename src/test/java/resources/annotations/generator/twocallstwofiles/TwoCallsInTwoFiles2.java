package resources.annotations.generator.twocallstwofiles;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;

public class TwoCallsInTwoFiles2 {
	public void generate(JspWriter out, String bad) throws IOException {
		out.write(TwoCallsInTwoFiles1.generateCode(bad));
	}
}
