package prompto.debug;

import java.util.Collection;


public interface IStackFrame {

	String getFilePath();
	String getMethodName();
	int getIndex();
	int getLine();
	int getStartCharIndex();
	int getEndCharIndex();
	boolean hasVariables();
	Collection<? extends IVariable> getVariables();

}
