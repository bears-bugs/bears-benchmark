package prompto.problem;

import org.antlr.v4.runtime.NoViableAltException;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;

public class ParserNoViableAltError extends ParserProblemBase {

	NoViableAltException e;
	
	public ParserNoViableAltError(String path, int line, int column, NoViableAltException e) {
		super(path, line, column);
		this.e = e;
	}
	
	@Override
	public int getStartIndex() {
		return e.getOffendingToken().getStartIndex();
	}
	
	@Override
	public int getEndIndex() {
		return getStartIndex() + getOffendingText().length();
	}
	
	String getOffendingText() {
		return e.getOffendingToken().getText();
	}
	
	@Override
	public String getMessage() {
		TokenStream tokens = (TokenStream)e.getInputStream();
		Token start = e.getStartToken();
		return "Invalid syntax at: " +
				( start.getType()==Token.EOF ? "<EOF>" : tokens.getText(start, e.getOffendingToken()));
	}
	
	@Override
	public Type getType() {
		return Type.ERROR;
	}
	
}
