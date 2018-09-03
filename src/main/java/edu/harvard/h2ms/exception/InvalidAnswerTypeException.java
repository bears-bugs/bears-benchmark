package edu.harvard.h2ms.exception;

/**
 * An exception thrown when a timeframe-related API end point receives an invalid value.
 */
public class InvalidAnswerTypeException extends Exception {
	public InvalidAnswerTypeException(String expected, String received) {
		super(String.format("Exepcted an answer type of %s, but received %s.", expected, received));
	}
}
