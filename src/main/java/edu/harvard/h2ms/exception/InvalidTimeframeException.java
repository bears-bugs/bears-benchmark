package edu.harvard.h2ms.exception;

/**
 * An exception thrown when a timeframe-related API end point receives an invalid value.
 */
public class InvalidTimeframeException extends Exception {
	public InvalidTimeframeException(String timeframe) {
		super(String.format("Timeframe must be one of week, month, quarter, year.  You asked for %s", timeframe));
	}
}
