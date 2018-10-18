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

import com.linkedin.pinot.core.data.partition.PartitionFunction;
import java.util.List;
import org.apache.commons.lang.math.IntRange;


/**
 * An interface to read the column statistics from statistics collectors.
 */
public interface ColumnStatistics {
    /**
     * @return Minimum value of the column
     */
    Object getMinValue();

    /**
     * @return Maximum value of the column
     */
    Object getMaxValue();

    /**
     *
     * @return An array of elements that has the unique values for this column, sorted order.
     */
    Object getUniqueValuesSet();

    /**
     *
     * @return The number of unique values of this column.
     */
    int getCardinality();

    /**
     *
     * @return For string objects, returns the length of the longest string value. For others, returns -1.
     */
    int getLengthOfLargestElement();

    /**
     * Whether or not the data in this column is in ascending order.
     * @return true if the data is in ascending order.
     */
    boolean isSorted();

    /**
     * @return total number of entries
     */
    int getTotalNumberOfEntries();

    /**
     * @return For multi-valued columns, returns the max number of values in a single occurrence of the column, otherwise 0.
     */
    int getMaxNumberOfMultiValues();

    /**
     * @return Returns if any of the values have nulls in the segments.
     */
    boolean hasNull();

    PartitionFunction getPartitionFunction();

    int getNumPartitions();

    List<IntRange> getPartitionRanges();

    /**
     * Returns the width of the partition range for this column, used when doing per-column partitioning.
     */
    int getPartitionRangeWidth();
}
