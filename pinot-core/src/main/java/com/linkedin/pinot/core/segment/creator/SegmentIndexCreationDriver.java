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
package com.linkedin.pinot.core.segment.creator;

import com.linkedin.pinot.core.indexsegment.generator.SegmentGeneratorConfig;
import java.io.File;


/**
 * Driver that creates and writes index segments to disk from data that is stored on disk.
 *
 * Nov 6, 2014
 */

public interface SegmentIndexCreationDriver {
  /**
   * Configures the segment generator with the given segment generator configuration, which contains the input file
   * location, format, schema and other necessary information to create an index segment.
   *
   * @param config The configuration to use when building an index segment
   */
  void init(SegmentGeneratorConfig config) throws Exception;

  /**
   * Builds an index segment and writes it to disk. The index segment creation extracts data from the input files,
   * profiles each column and then builds indices based on the profiling information gathered.
   */
  void build() throws Exception;

  String getSegmentName() ;

  /**
   *  Get the stats collector for a column
   *
   * @param columnName
   * @return AbstractColumnStatisticsCollector for the column.
   * @throws Exception
   */
  ColumnStatistics getColumnStatisticsCollector(final String columnName) throws Exception;

  /**
   * Returns the path of the output directory
   */
  File getOutputDirectory();
}
