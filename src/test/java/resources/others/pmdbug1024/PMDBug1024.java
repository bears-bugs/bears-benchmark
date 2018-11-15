package resources.others.pmdbug1024;

import java.util.List;



// http://sourceforge.net/p/pmd/bugs/1024/
// Can cause pmd to hang on DFA analisys

public class PMDBug1024 {

	
	
	public void testFunction(List<String> employees, boolean isBatchMode) throws Exception {
		StringBuilder builder = new StringBuilder();
		for (String employee : employees) {
			try {
				builder.append(employee);
			} catch (Exception e) {
				if (isBatchMode) {
					log("Error!" + employee);
				} else {
					throw e;
				}
			}
			if (builder.length() == 0) {
				builder.append("NoResults");
			}
		}
	}
	private static void log(String a) {
		// log to somewhere
	}

}
