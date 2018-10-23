/*
 * Copyright 2016 MiLaboratory.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.milaboratory.util;

import java.text.DecimalFormat;
import java.util.concurrent.atomic.AtomicLong;

public abstract class AbstractLongProcessReporter implements LongProcessReporter {
    /**
     * 1 second
     */
    public static final long DEFAULT_REPORTING_INTERVAL = 1_000_000_000L;
    public static final double DEFAULT_REPORTING_PROGRESS_DELTA = 0.1;

    /**
     * Counter of long processes
     */
    final AtomicLong counter = new AtomicLong();
    /**
     * Minimal reporting interval in nanoseconds
     */
    final long minimalReportingInterval;
    /**
     * Minimal reporting progress interval
     */
    final double minimalReportingProgressDelta;

    public AbstractLongProcessReporter() {
        this(DEFAULT_REPORTING_INTERVAL, DEFAULT_REPORTING_PROGRESS_DELTA);
    }

    public AbstractLongProcessReporter(long minimalReportingInterval,
                                       double minimalReportingProgressDelta) {
        this.minimalReportingInterval = minimalReportingInterval;
        this.minimalReportingProgressDelta = minimalReportingProgressDelta;
    }

    public abstract void report(long id, String name, double progress);

    @Override
    public LongProcess start(final String name) {
        final long id = counter.getAndIncrement();
        report(id, name, 0.0);
        return new LongProcess() {
            final AtomicLong previousTimestamp = new AtomicLong(System.nanoTime());
            volatile double previousProgress = 0.0;

            @Override
            public void reportStatus(double progress) {
                if (progress < previousProgress + minimalReportingProgressDelta)
                    return;
                long timestamp = System.nanoTime();
                long pt = previousTimestamp.get();
                if (timestamp < pt + minimalReportingInterval)
                    return;
                if (previousTimestamp.compareAndSet(pt, timestamp)) {
                    previousProgress = progress;
                    report(id, name, progress);
                }
            }

            @Override
            public void close() {
                report(id, name, Double.POSITIVE_INFINITY);
            }
        };
    }

    private static final DecimalFormat percentFormat = new DecimalFormat("##.#'%'");

    public static LongProcessReporter stderrReporter() {
        return new AbstractLongProcessReporter() {
            @Override
            public void report(long id, String name, double progress) {
                String status = Double.POSITIVE_INFINITY == progress ? "done" :
                        percentFormat.format(progress * 100.0);
                System.err.println(name + ": " + status);
            }
        };
    }
}
