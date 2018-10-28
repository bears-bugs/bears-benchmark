package prompto.parser;

public interface ISection {
	
	String getFilePath();
	ILocation getStart();
	ILocation getEnd();
	Dialect getDialect();
	void setAsBreakpoint(boolean set);
	boolean isBreakpoint();
	boolean isOrContains(ISection section);
	
}
