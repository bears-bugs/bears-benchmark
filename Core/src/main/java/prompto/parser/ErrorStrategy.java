package prompto.parser;

import org.antlr.v4.runtime.DefaultErrorStrategy;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.IntervalSet;

/* we need to override the DefaultErrorStrategy which does not produce structured data for the below */
public class ErrorStrategy extends DefaultErrorStrategy {

	@Override
	protected void reportUnwantedToken(Parser recognizer) {
		if (inErrorRecoveryMode(recognizer)) {
			return;
		}

		beginErrorCondition(recognizer);

		Token t = recognizer.getCurrentToken();
		String tokenName = getTokenErrorDisplay(t);
		IntervalSet expecting = getExpectedTokens(recognizer);
		String msg = "extraneous input "+tokenName+" expecting "+ expecting.toString(recognizer.getVocabulary());
		UnwantedTokenException e = new UnwantedTokenException(recognizer, recognizer.getInputStream(), recognizer.getContext(), t);
		recognizer.notifyErrorListeners(t, msg, e);
	}
	
	@Override
	protected void reportMissingToken(Parser recognizer) {
		if (inErrorRecoveryMode(recognizer)) {
			return;
		}

		beginErrorCondition(recognizer);

		Token t = recognizer.getCurrentToken();
		IntervalSet expecting = getExpectedTokens(recognizer);
		String msg = "missing " + expecting.toString(recognizer.getVocabulary()) + " at " + getTokenErrorDisplay(t);
		MissingTokenException e = new MissingTokenException(recognizer, recognizer.getInputStream(), recognizer.getContext(), t, expecting);
		recognizer.notifyErrorListeners(t, msg, e);
	}
}
