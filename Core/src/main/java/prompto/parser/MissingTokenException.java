package prompto.parser;

import org.antlr.v4.runtime.IntStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.IntervalSet;

public class MissingTokenException extends RecognitionException {

	private static final long serialVersionUID = 1L;

	IntervalSet expected;
	Token actual;
	
	public MissingTokenException(Recognizer<?, ?> recognizer, IntStream input, ParserRuleContext ctx, Token actual, IntervalSet expected) {
		super(recognizer, input, ctx);
		this.actual = actual;
		this.expected = expected;
	}

	public int getStartIndex() {
		return actual.getStartIndex();
	}

	public String getOffendingText() {
		return actual.getText();
	}

	public String getMissingTokensAsString() {
		String result = expected.toString(this.getRecognizer().getVocabulary());
		return result.replace('{', '[').replace('}', ']');
	}

}
