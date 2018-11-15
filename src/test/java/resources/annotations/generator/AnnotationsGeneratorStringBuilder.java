package resources.annotations.generator;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;

import com.gdssecurity.pmd.annotations.HTMLGenerator;

public class AnnotationsGeneratorStringBuilder {

	@HTMLGenerator
	public static StringBuilder generateOkBuilder(String param) {
		return new StringBuilder("ok");
	}
	@HTMLGenerator
	public static StringBuilder generateBadBuilder(String param) {
		StringBuilder sb = new StringBuilder();
		sb.append("ok");
		sb.append(param);
		return sb;
	}
	
	@HTMLGenerator
	public static StringBuffer generateOkBuffer(String param) {
		StringBuffer sb = new StringBuffer();
		sb.append("ok");
		return sb;
	}
	@HTMLGenerator
	public static StringBuffer generateBadBuffer(String param) {
		StringBuffer sb = new StringBuffer();
		sb.append("ok");
		sb.append(param);
		return sb;
	}
	
	public static void testCall(JspWriter out, String dirty) throws IOException  {
		out.write(generateOkBuffer(dirty).toString());
	}
	
	public static void testCall2(JspWriter out, String dirty) throws IOException  {
		out.write(generateOkBuilder(dirty).toString());
	}
	public static void testCall3(JspWriter out, String dirty) throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append("ok");
		sb.append(generateOkBuilder(dirty));
		out.write(sb.toString());
	}
	
	
}
