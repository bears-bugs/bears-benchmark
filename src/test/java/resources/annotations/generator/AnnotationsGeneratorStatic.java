package resources.annotations.generator;

import com.gdssecurity.pmd.annotations.HTMLGenerator;

public class AnnotationsGeneratorStatic {

	
	@HTMLGenerator
	public static String getHTML(String param) {
		StringBuilder a = new StringBuilder();
		a.append(param);
		return a.toString();
	}
}
