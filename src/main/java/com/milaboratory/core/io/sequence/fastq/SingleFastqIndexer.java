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
package com.milaboratory.core.io.sequence.fastq;

import cc.redberry.pipe.OutputPortCloseable;
import com.milaboratory.core.io.sequence.SingleRead;
import com.milaboratory.core.io.sequence.SingleReader;
import com.milaboratory.core.io.util.FileIndex;
import com.milaboratory.core.io.util.FileIndexBuilder;
import com.milaboratory.util.CanReportProgress;

public class SingleFastqIndexer implements SingleReader,
        CanReportProgress, OutputPortCloseable<SingleRead> {
    private final SingleFastqReader reader;
    private final FileIndexBuilder indexBuilder;

    public SingleFastqIndexer(SingleFastqReader reader, long step) {
        this.reader = reader;
        this.indexBuilder = new FileIndexBuilder(step);
    }

    public SingleFastqReader setTotalSize(long totalSize) {
        return reader.setTotalSize(totalSize);
    }

    @Override
    public double getProgress() {
        return reader.getProgress();
    }

    @Override
    public boolean isFinished() {
        return reader.isFinished();
    }

    @Override
    public SingleRead take() {
        SingleRead read = reader.take();
        if (read == null)
            return null;
        indexBuilder.appendNextRecord(reader.recordsReader.qualityEnd - reader.recordsReader.descriptionBegin + 2);
        return read;
    }

    @Override
    public long getNumberOfReads() {
        return reader.getNumberOfReads();
    }

    @Override
    public void close() {
        reader.close();
    }

    public FileIndex createIndex() {
        indexBuilder.putMetadata("format", reader.getQualityFormat().toString());
        return indexBuilder.createAndDestroy();
    }

    public SingleFastqIndexer readToEnd() {
        while (take() != null) ;
        return this;
    }
}
