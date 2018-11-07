package org.jsapar.convert;

import org.jsapar.error.ErrorEventListener;
import org.jsapar.model.Line;

import java.io.IOException;
import java.util.List;

/**
 * Abstract base class for all converters.
 * <p>
 * By adding
 * a LineManipulator you are able to make modifications of each line before it is written to the
 * output. The method {@link LineManipulator#manipulate(Line)} of all added LineManipulators are called for each line that are
 * parsed successfully.
 * <p>
 * For each line, the line type of the parsed line is
 * considered when choosing the line type of the output schema line. This means that lines with a
 * type that does not exist in the output schema will be discarded in the output.
 * <p>
 * If your want lines to be discarded from the output depending of their contents, add a LineManipulator that returns
 * false for the lines that should not be composed.
 * <p>
 * The default error handling is to throw an exception upon the first error that occurs. You can however change that
 * behavior by adding an {@link org.jsapar.error.ErrorEventListener}. There are several implementations to choose from such as
 * {@link org.jsapar.error.RecordingErrorEventListener} or
 * {@link org.jsapar.error.ThresholdRecordingErrorEventListener}, or you may implement your own.
 */
public abstract class AbstractConverter {

    private List<LineManipulator> manipulators = new java.util.LinkedList<>();
    private ErrorEventListener errorListener;

    public AbstractConverter() {
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
     * Replaces existing error event listener.
     * @param errorListener The new error event listener to use.
     */
    public void setErrorEventListener(ErrorEventListener errorListener) {
        this.errorListener = errorListener;
    }

    /**
     * Executes the convert task and applies all manipulators in order..
     * @param convertTask The convert task to execute.
     * @return The number of lines converted.
     * @throws IOException In case of io error while writing output.
     */
    protected long execute(ConvertTask convertTask) throws IOException {
        manipulators.forEach(convertTask::addLineManipulator);
        if(errorListener != null)
            convertTask.setErrorEventListener(errorListener);
        return convertTask.execute();
    }

}
