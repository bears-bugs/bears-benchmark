package resources.cwe22pathtraversal;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

public class PathTraversalExample {

	public void bad(HttpServletRequest request) {
		String fileName = request.getParameter("name");
		new File("/base/path/" + fileName);
//		f.getAbsolutePath();
	}
	
	public void bad2(String param) {
		File f = new File(param);
		f.getAbsolutePath();
	}
	
}
