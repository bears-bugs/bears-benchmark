/*
 * (C) Copyright 2005-2018, by Assaf Lehr and Contributors.
 *
 * JGraphT : a free Java graph-theory library
 *
 * See the CONTRIBUTORS.md file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0, or the
 * GNU Lesser General Public License v2.1 or later
 * which is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1-standalone.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR LGPL-2.1-or-later
 */
package org.jgrapht.util;

import java.util.concurrent.*;

/**
 * A very simple stop watch.
 * 
 * @author Assaf Lehr
 */
public class StopWatch
{
    private long startTime;

    /**
     * Construct a new stop watch and start it.
     */
    public StopWatch()
    {
        start();
    }

    /**
     * Restart.
     */
    public void start()
    {
        this.startTime = System.nanoTime();
    }

    /**
     * Get the elapsed time from the last restart.
     * 
     * @param timeUnit the time unit
     * @return the elapsed time in the given time unit
     */
    public long getElapsed(TimeUnit timeUnit)
    {
        return timeUnit.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
    }

}

