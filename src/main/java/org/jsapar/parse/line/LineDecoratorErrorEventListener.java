package org.jsapar.parse.line;

import org.jsapar.error.ErrorEvent;
import org.jsapar.error.ErrorEventListener;
import org.jsapar.model.Line;
import org.jsapar.parse.CellParseException;
import org.jsapar.parse.LineParseException;

/**
 * Internal class. Decorates line errors with current line information.
 */
public class LineDecoratorErrorEventListener implements ErrorEventListener {

    private  ErrorEventListener errorListener;
    private       Line               line;

    public LineDecoratorErrorEventListener(){}

    public void initialize(ErrorEventListener errorListener, Line line) {
        this.errorListener = errorListener;
        this.line = line;
    }

    @Override
    public void errorEvent(ErrorEvent event) {
        if(event.getError() instanceof CellParseException) {
            ((CellParseException) event.getError()).setLineNumber(line.getLineNumber());
            line.addCellError((CellParseException) event.getError());
        }
        else if(event.getError() instanceof LineParseException) {
            ((LineParseException) event.getError()).setLineNumber(line.getLineNumber());
        }
        errorListener.errorEvent(event);
    }

}
