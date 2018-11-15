package resources.others.bug28;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;

public class ViolationAfterCatch {

//	public void testMethod2(String param, JspWriter out) throws Exception {
//
//		try {
//			Integer.parseInt("5");
//		} catch (Exception e) {
//			if (e instanceof RuntimeException) {
//				throw e;
//			} else {
//				throw new Exception(e.getMessage());
//			}
//		}
//		// This is a violation, param directly written to JspWriter
//		out.print(param);
//
//	}

	public void testMethod(HttpServletRequest request, JspWriter out) throws Exception {

		String param = request.getParameter("param");

		try {
			Integer.parseInt("5");
		} catch (Exception e) {
			if (e instanceof RuntimeException) {
				throw e;
			} else {
				throw new Exception(e.getMessage());
			}
		}
		// This is a violation, param directly written to JspWriter
		out.print(param);

	}

}
