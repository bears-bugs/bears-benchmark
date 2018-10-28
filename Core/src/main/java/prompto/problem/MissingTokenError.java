package prompto.problem;

import prompto.parser.MissingTokenException;

public class MissingTokenError extends ParserProblemBase {

	MissingTokenException e;
	
	public MissingTokenError(String path, int line, int column, MissingTokenException e) {
		super(path, line, column);
		this.e = e;
	}
	
	@Override
	public int getStartIndex() {
		return e.getStartIndex();
	}
	
	@Override
	public int getEndIndex() {
		return getStartIndex();
	}
	
	@Override
	public String getMessage() {
		return "Missing token, expecting: " + e.getMissingTokensAsString() 
				+ ", found: " + e.getOffendingText() + " instead.";
	}
	
	@Override
	public Type getType() {
		return Type.ERROR;
	}
	
}
