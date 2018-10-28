package prompto.problem;

import org.antlr.v4.runtime.LexerNoViableAltException;
import org.antlr.v4.runtime.misc.Interval;

public class LexerNoViableAltError extends ParserProblemBase {

	LexerNoViableAltException e;
	
	public LexerNoViableAltError(String path, int line, int column, LexerNoViableAltException e) {
		super(path, line, column);
		this.e = e;
	}
	
	@Override
	public int getStartIndex() {
		return e.getStartIndex();
	}
	
	@Override
	public int getEndIndex() {
		return getStartIndex() + getOffendingText().length();
	}
	
	String getOffendingText() {
		int idx = getStartIndex();
		return e.getInputStream().getText(Interval.of(idx, idx));
	}
	
	@Override
	public String getMessage() {
		return "Unrecognized character sequence: " + getOffendingText();
	}
	
	@Override
	public Type getType() {
		return Type.ERROR;
	}
	
}
