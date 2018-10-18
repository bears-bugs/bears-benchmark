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
package com.linkedin.pinot.core.query.aggregation.groupby;

/**
 * Interface for ResultHolder to store results of GroupByAggregation.
 */
public interface GroupByResultHolder {
  int[] EMPTY_ARRAY = {};

  // Array mode for size <= _maxCapacity, map mode otherwise.
  enum StorageMode {
    ARRAY_STORAGE,
    MAP_STORAGE
  }

  /**
   * Stores the given value (of type double) for the given groupKey.
   *
   * @param groupKey
   * @param value
   */
  void setValueForKey(int groupKey, double value);

  /**
   * Store the given value (of type ResultType) for the given groupKey.
   * @param groupKey
   * @param value
   */
  void setValueForKey(int groupKey, Object value);

  /**
   * Returns the result (double) for the given group by key.
   * If the group key does not exist in the result holder, returns
   * the defaultValue it was initialized with (default value of the aggregation
   * function it is holding the result for).
   *
   * @param groupKey
   * @return
   */
  double getDoubleResult(int groupKey);

  /**
   * Returns the result (ResultType) for the given group key.
   * If the group key does not exist in the result holder, returns the
   * defaultValue it was initialized with (default value of the aggregation
   * function it is holding the result for).
   *
   * @param groupKey
   * @return
   */
  <T> T getResult(int groupKey);


  /**
   * Increase internal storage if needed to store the required number
   * of unique group keys.
   *
   * @param capacity
   */
  void ensureCapacity(int capacity);

  /**
   * Trim the results to a pre-specified size.
   * @return List of group keys that were removed.
   */
  int[] trimResults();
}
