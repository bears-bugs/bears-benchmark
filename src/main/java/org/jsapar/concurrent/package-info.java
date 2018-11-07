/**
 * Can be used to add concurrency. Use {@link org.jsapar.concurrent.ConcurrentConvertTask}
 * to run the {@link org.jsapar.compose.Composer} in a different thread than the parser. Please note that by using
 * concurrency, you also introduce a lot more complexity. Concurrency is only needed when parsing really large sources
 * or when both source and target are really slow. Also note that the initialization time for these classes is higher than for the
 * single threaded versions.
 *
 * As a rule of thumb while working with normal files on disc, don't use these concurrent versions unless your input
 * normally exceeds at least 1MB of data, as the overhead of starting
 * a new thread and synchronizing threads are otherwise greater than the gain by the concurrency.
 *
 */
package org.jsapar.concurrent;