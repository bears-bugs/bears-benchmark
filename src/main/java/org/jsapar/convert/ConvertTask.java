package org.jsapar.convert;

import org.jsapar.compose.Composer;
import org.jsapar.error.ErrorEventListener;
import org.jsapar.model.Line;
import org.jsapar.parse.LineEventListener;
import org.jsapar.parse.LineParsedEvent;
import org.jsapar.parse.ParseTask;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;

/**
 * Reads from supplied parseTask and outputs each line to the composer. By adding
 * a LineManipulator you are able to make modifications of each line before it is written to the
 * output. The method {@link LineManipulator#manipulate(Line)} of all added LineManipulators are called for each line that is
 * parsed successfully.
 * <p>
 * For each line, the line type of the parsed line is
 * considered when choosing the line type of the output schema line. This means that lines with a
 * type that does not exist in the output schema will be discarded in the output.
 * <p>
 * If your want lines to be discarded from the output depending of their contents, add a LineManipulator that returns
 * false for the lines that should not be composed.
 */
public class ConvertTask {
    private ParseTask parseTask;
    private Composer  composer;
    private List<LineManipulator> manipulators = new java.util.LinkedList<>();

    /**
     * Creates a convert task with supplied parse task and composer.
     * @param parseTask The parse task to use.
     * @param composer The composer to use.
     */
    public ConvertTask(ParseTask parseTask, Composer composer) {
        this.parseTask = parseTask;
        this.composer = composer;
    }

    /**
     * Sets new error listener to both the parse task and the composer. Can be called after creation but before calling
     * {@link #execute()}.
     *
     * @param errorListener The new error event listener to use for both parsing and composing.
     */
    public void setErrorEventListener(ErrorEventListener errorListener) {
        this.parseTask.setErrorEventListener(errorListener);
        this.composer.setErrorEventListener(errorListener);
    }

    /**
     * Adds LineManipulator to this converter. All present line manipulators are executed for each
     * line.
     *
     * @param manipulator The line manipulator to add.
     */
    public void addLineManipulator(LineManipulator manipulator) {
        manipulators.add(manipulator);
    }

    /**
     * @return Number of converted lines.
     * @throws IOException In case of IO error.
     */
    public long execute() throws IOException {
        try {
            parseTask.setLineEventListener(new LineForwardListener());
            return parseTask.execute();
        }catch (UncheckedIOException e){
            throw e.getCause() != null ? e.getCause() : new IOException(e);
        }
    }

    /**
     * Internal class for handling output of one line at a time while receiving parsing events.
     *
     */
    protected class LineForwardListener implements LineEventListener {

        public LineForwardListener() {
        }

        @Override
        public void lineParsedEvent(LineParsedEvent event) {
            Line line = event.getLine();
            for (LineManipulator manipulator : manipulators) {
                if (!manipulator.manipulate(line))
                    return;
            }
            composer.composeLine(line);
        }
    }

    public ParseTask getParseTask() {
        return parseTask;
    }

    public Composer getComposer() {
        return composer;
    }
}
