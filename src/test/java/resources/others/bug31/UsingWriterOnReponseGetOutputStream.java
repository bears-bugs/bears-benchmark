package resources.others.bug31;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UsingWriterOnReponseGetOutputStream extends HttpServlet {

	private static final long serialVersionUID = 7570602756272425448L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String badParam = request.getParameter("param");
		Writer out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), StandardCharsets.UTF_8));
		out.write(" <input type='hidden' name='test' value='" + badParam + "' />\n");
		out.close();
	}
}
