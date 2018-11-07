package org.jsapar.parse;

import org.jsapar.error.JSaParException;

/**
 * Exception that occurs while parsing and that affects the line and not a single cell.
 */
@SuppressWarnings("WeakerAccess")
public class LineParseException extends JSaParException {
    private long      lineNumber;

    /** Creates a new line parse exception.
     * @param lineNumber The line number where the error occured
     * @param errorDescription A message.
     */
    public LineParseException(long lineNumber, String errorDescription) {
        super(errorDescription);

        this.lineNumber = lineNumber;
    }

    public LineParseException(long lineNumber, Throwable cause) {
        super(cause);

        this.lineNumber = lineNumber;
    }

    /**
     * @return the lineNumber
     */
    public long getLineNumber() {
        return lineNumber;
    }

    /**
     * @return A simple message describing the error and it's location.
     */
    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder();
        if(this.getLineNumber()>0) {
            sb.append("Line=");
            sb.append(this.getLineNumber());
            sb.append(" - ");
        }
        sb.append(super.getMessage());
        return sb.toString();
    }

    protected String getOriginalMessage(){
        return super.getMessage();
    }

    public void setLineNumber(long lineNumber) {
        this.lineNumber = lineNumber;
    }
}
