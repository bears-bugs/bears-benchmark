package resources.annotations.generator;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;

public class AnnotationsGeneratorStaticCaller {

	
	public void write(JspWriter out, String dirty) throws IOException {
		String html = AnnotationsGeneratorStatic.getHTML(dirty);
		out.print(html);
	}
}
