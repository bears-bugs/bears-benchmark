/*
 * Copyright 2015 MiLaboratory.com
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
package com.milaboratory.core.io.sequence;

import com.milaboratory.util.CanReportProgress;

/**
 * Created by dbolotin on 23/06/14.
 */
public abstract class AbstractMultiReader<R extends SequenceRead>
        extends AbstractSequenceReader<R> implements CanReportProgress,
        SequenceReader<R>, SequenceReaderCloseable<R> {
    private final SingleReader[] readers;
    private final CanReportProgress[] progressReporters;

    public AbstractMultiReader(SingleReader... readers) {
        for (SingleReader reader : readers)
            if (reader == null)
                throw new NullPointerException();

        this.readers = readers;
        boolean crp = true;
        for (SingleReader reader : readers)
            crp &= reader instanceof CanReportProgress;
        if (crp) {
            progressReporters = new CanReportProgress[readers.length];
            for (int i = 0; i < readers.length; i++)
                progressReporters[i] = (CanReportProgress) readers[i];
        } else
            progressReporters = null;

    }

    protected synchronized SingleRead[] takeReads() {
        SingleRead[] reads = new SingleRead[readers.length];

        boolean hasNulls = false, allNulls = true;
        for (int i = 0; i < reads.length; i++) {
            reads[i] = readers[i].take();
            hasNulls |= (reads[i] == null);
            allNulls &= (reads[i] == null);
        }

        if (allNulls)
            return null;

        if (hasNulls)
            throw new RuntimeException("Different number of reads in single-readers.");

        // Incrementing reads counter
        addOneRead();

        return reads;
    }

    @Override
    public void close() {
        RuntimeException exception = null;

        for (SingleReader reader : readers)
            if (reader != null)
                try {
                    reader.close();
                } catch (RuntimeException e) {
                    exception = e;
                }

        if (exception != null)
            throw exception;
    }

    @Override
    public double getProgress() {
        if (progressReporters == null)
            return Double.NaN;

        double sum = 0.0;
        for (CanReportProgress reporter : progressReporters) {
            double progress = reporter.getProgress();
            if (Double.isNaN(progress))
                return Double.NaN;
            sum += progress;
        }

        return sum / progressReporters.length;
    }

    @Override
    public boolean isFinished() {
        if (progressReporters == null)
            return true;

        boolean allFinished = true;
        for (CanReportProgress reporter : progressReporters)
            allFinished &= reporter.isFinished();

        return allFinished;
    }
}
