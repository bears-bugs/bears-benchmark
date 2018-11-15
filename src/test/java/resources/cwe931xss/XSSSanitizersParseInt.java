package resources.cwe931xss;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;

public class XSSSanitizersParseInt {

	public XSSSanitizersParseInt() {
		super();
	}

	public void executeGood(JspWriter writer, String param) throws IOException {
		String[] i_anualidad_sg_cert = { "a", "b", "c", "d", "e" };
		writer.print(i_anualidad_sg_cert[Integer.parseInt(param)]);
//		writer.print(i_anualidad_sg_cert[Integer.parseInt(param) - 1]);
//		writer.print(Integer.parseInt(param) - 1);
//		writer.print(Integer.parseInt(param));
//		int pos = Integer.parseInt(param);
//		writer.print(i_anualidad_sg_cert[pos]);
	}
}
