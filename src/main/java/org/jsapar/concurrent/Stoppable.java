/*
 * Stoppable.java
 * 
 * Created on 12 December 2007 14:15
 */

/* Revision History:
 *
 * $Log: Stoppable.java,v $
 * Revision 1.1  2007/12/18 09:10:58  koikaj0
 * a shutdown interface
 *
 */
package org.jsapar.concurrent;

/**
 * Adds a process/thread stop interface. The implementation of stop() should in a suitable way stop
 * the running thread as in:
 * 
 * <pre>
 * {@code
 * public class MyClass implements Runnable, Stoppable
 * {
 *     ...
 *     public void stop()
 *     {
 *         doRun = false; // stop the run loop
 *     }
 *     ...
 *     public void run()
 *     {
 *         ...
 *         while (doRun)
 *         {
 *         }
 *         ...
 *     }
 * }
 * }
 * </pre>
 * 
 */
public interface Stoppable {

    /**
     * Called by e.g. the ShutdownHook to signal that the thread should shut down. Implement this
     * method with code that shuts down the thread gracefully.
     */
    void stop();
}
