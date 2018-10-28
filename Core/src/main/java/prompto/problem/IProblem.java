package prompto.problem;

public interface IProblem {
	
	String getPath();
	int getStartIndex();
	int getEndIndex();
	int getStartLine();
	int getStartColumn();
	String getMessage();
	Type getType();
	
	static enum Type {
		// match Eclipse SEVERITY
		INFO,		// public static final int SEVERITY_INFO = 0;
		WARNING, 	// public static final int SEVERITY_WARNING = 1;
		ERROR		// public static final int SEVERITY_ERROR = 2;
		
	}
}
