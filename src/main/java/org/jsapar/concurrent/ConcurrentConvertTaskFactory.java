package org.jsapar.concurrent;

import org.jsapar.compose.Composer;
import org.jsapar.convert.AbstractConverter;
import org.jsapar.convert.ConvertTask;
import org.jsapar.parse.ParseTask;
import org.jsapar.schema.Schema;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Internal class for creating concurrent convert task with registered runnable. Makes it possible to first register
 * runnables and then create many convert tasks using the same runnables.
 *
 * @see ConcurrentConvertTask
 *
 */
public class ConcurrentConvertTaskFactory implements ConcurrentStartStop{
    private final List<Runnable>  onStart     = new LinkedList<>();
    private final List<Runnable>  onStop      = new LinkedList<>();


    /**
     * @param parseTask The parse task to use
     * @param composer The composer to use
     * @return Number of converted lines.
     */
    protected ConvertTask makeConvertTask(ParseTask parseTask, Composer composer)  {
        ConcurrentConvertTask convertTask = new ConcurrentConvertTask(parseTask, composer);
        onStart.forEach(convertTask::registerOnStart);
        onStop.forEach(convertTask::registerOnStop);
        return convertTask;
    }


    public void registerOnStart(Runnable onStart){
        this.onStart.add(onStart);
    }

    public void registerOnStop(Runnable onStop){
        this.onStop.add(onStop);
    }

}
