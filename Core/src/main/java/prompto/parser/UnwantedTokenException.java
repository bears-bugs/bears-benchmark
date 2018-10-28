package prompto.parser;

import org.antlr.v4.runtime.IntStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;

public class UnwantedTokenException extends RecognitionException {

	private static final long serialVersionUID = 1L;

	Token token;
	
	public UnwantedTokenException(Recognizer<?, ?> recognizer, IntStream input, ParserRuleContext ctx, Token t) {
		super(recognizer, input, ctx);
		this.token = t;
	}

	public int getStartIndex() {
		return token.getStartIndex();
	}

	public String getOffendingText() {
		return token.getText();
	}

}
