/**
 * Copyright (C) 2014-2016 LinkedIn Corp. (pinot-core@linkedin.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.linkedin.pinot.core.realtime.converter.stats;

import com.linkedin.pinot.common.data.Schema;
import com.linkedin.pinot.core.data.readers.RecordReader;
import com.linkedin.pinot.core.realtime.converter.RealtimeSegmentRecordReader;
import com.linkedin.pinot.core.realtime.impl.RealtimeSegmentImpl;
import com.linkedin.pinot.core.segment.creator.SegmentCreationDataSource;
import com.linkedin.pinot.core.segment.creator.SegmentPreIndexStatsContainer;
import com.linkedin.pinot.core.segment.creator.StatsCollectorConfig;


/**
 * Segment creation data source that is based on an in-memory realtime segment.
 */
public class RealtimeSegmentSegmentCreationDataSource implements SegmentCreationDataSource {
  private final RealtimeSegmentImpl _realtimeSegment;
  private final RealtimeSegmentRecordReader _realtimeSegmentRecordReader;
  private final Schema _schema;

  public RealtimeSegmentSegmentCreationDataSource(RealtimeSegmentImpl realtimeSegment, RealtimeSegmentRecordReader realtimeSegmentRecordReader, Schema schema) {
    _realtimeSegment = realtimeSegment;
    _realtimeSegmentRecordReader = realtimeSegmentRecordReader;
    _schema = schema;
  }

  @Override
  public SegmentPreIndexStatsContainer gatherStats(StatsCollectorConfig statsCollectorConfig) {
    if (!statsCollectorConfig.getSchema().equals(_schema)) {
      throw new RuntimeException("Incompatible schemas used for conversion and extraction");
    }

    return new RealtimeSegmentStatsContainer(_realtimeSegment, _realtimeSegmentRecordReader);
  }

  @Override
  public RecordReader getRecordReader() {
    return _realtimeSegmentRecordReader;
  }
}
