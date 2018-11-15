package resources.cwe931xss;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

public class TestXSSServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * Variable declaration (v1) and initialization with tainted source of input
	 * Second variable declared (w1) and assigned tainted variable (v1) Tainted
	 * variable w1 passed to XSS sink This pattern creates XSS vulnerability
	 * 
	 */

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		PrintWriter pw = response.getWriter();
		try {
			String v1 = request.getParameter("p1");
			String w1 = v1;
			pw.print(w1);
		} finally {
			IOUtils.closeQuietly(pw);
		}

	}

}
