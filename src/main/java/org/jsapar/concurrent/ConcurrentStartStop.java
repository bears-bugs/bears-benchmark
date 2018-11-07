package org.jsapar.concurrent;

/**
 * Common interface for all concurrent classes that can have registered on-start and on-stop runnables.
 */
public interface ConcurrentStartStop {
    /**
     * Each registered onStart runnable will be called in the same order that they were registered by consumer thread
     * when it starts up but before it starts handling any event. Use this in order to
     * implement initialization needed for the new
     * thread.
     *
     * @param onStart The runnable that will be called by consumer thread when starting up.
     */
    void registerOnStart(Runnable onStart);

    /**
     * Each registered onStop runnable will be called in the same order that they were registered by consumer
     * thread just before it dies. Use this in order to
     * implement resource deallocation etc. These handlers are called also when the thread is terminated with an exception so
     * be aware that you may end up here also when a serious error has occurred.
     *
     * @param onStop The runnable that will be called by consumer thread when stopping.
     */
    void registerOnStop(Runnable onStop);
}
