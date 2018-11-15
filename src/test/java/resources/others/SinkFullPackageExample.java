package resources.others;

import java.io.IOException;

import javax.servlet.jsp.*;


public class SinkFullPackageExample {

	public void function(JspWriter writer, String badValue) throws IOException{
		writer.write(badValue);
	}
}
