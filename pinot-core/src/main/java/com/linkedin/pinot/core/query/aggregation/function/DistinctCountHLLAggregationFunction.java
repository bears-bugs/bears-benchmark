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
package com.linkedin.pinot.core.query.aggregation.function;

import com.clearspring.analytics.stream.cardinality.CardinalityMergeException;
import com.clearspring.analytics.stream.cardinality.HyperLogLog;
import com.linkedin.pinot.common.data.FieldSpec;
import com.linkedin.pinot.core.common.BlockValSet;
import com.linkedin.pinot.core.query.aggregation.AggregationResultHolder;
import com.linkedin.pinot.core.query.aggregation.ObjectAggregationResultHolder;
import com.linkedin.pinot.core.query.aggregation.groupby.GroupByResultHolder;
import com.linkedin.pinot.core.query.aggregation.groupby.ObjectGroupByResultHolder;
import com.linkedin.pinot.startree.hll.HllConstants;
import javax.annotation.Nonnull;


public class DistinctCountHLLAggregationFunction implements AggregationFunction<HyperLogLog, Long> {
  private static final String NAME = AggregationFunctionFactory.AggregationFunctionType.DISTINCTCOUNTHLL.getName();

  @Nonnull
  @Override
  public String getName() {
    return NAME;
  }

  @Nonnull
  @Override
  public String getColumnName(@Nonnull String[] columns) {
    return NAME + "_" + columns[0];
  }

  @Override
  public void accept(@Nonnull AggregationFunctionVisitorBase visitor) {
    visitor.visit(this);
  }

  @Nonnull
  @Override
  public AggregationResultHolder createAggregationResultHolder() {
    return new ObjectAggregationResultHolder();
  }

  @Nonnull
  @Override
  public GroupByResultHolder createGroupByResultHolder(int initialCapacity, int maxCapacity, int trimSize) {
    return new ObjectGroupByResultHolder(initialCapacity, maxCapacity, trimSize);
  }

  @Override
  public void aggregate(int length, @Nonnull AggregationResultHolder aggregationResultHolder,
      @Nonnull BlockValSet... blockValSets) {

    HyperLogLog hyperLogLog = aggregationResultHolder.getResult();
    if (hyperLogLog == null) {
      hyperLogLog = new HyperLogLog(HllConstants.DEFAULT_LOG2M);
      aggregationResultHolder.setValue(hyperLogLog);
    }

    FieldSpec.DataType valueType = blockValSets[0].getValueType();
    switch (valueType) {
      case INT:
        int[] intValues = blockValSets[0].getIntValuesSV();
        for (int i = 0; i < length; i++) {
          hyperLogLog.offer(intValues[i]);
        }
        break;

      case LONG:
        long[] longValues = blockValSets[0].getLongValuesSV();
        for (int i = 0; i < length; i++) {
          hyperLogLog.offer(Long.valueOf(longValues[i]).hashCode());
        }
        break;

      case FLOAT:
        float[] floatValues = blockValSets[0].getFloatValuesSV();
        for (int i = 0; i < length; i++) {
          hyperLogLog.offer(Float.valueOf(floatValues[i]).hashCode());
        }
        break;

      case DOUBLE:
        double[] doubleValues = blockValSets[0].getDoubleValuesSV();
        for (int i = 0; i < length; i++) {
          hyperLogLog.offer(Double.valueOf(doubleValues[i]).hashCode());
        }
        break;

      case STRING:
        String[] stringValues = blockValSets[0].getStringValuesSV();
        for (int i = 0; i < length; i++) {
          hyperLogLog.offer(stringValues[i]);
        }
        break;

      default:
        throw new IllegalArgumentException("Illegal data type for distinct count aggregation function: " + valueType);
    }
  }

  @Override
  public void aggregateGroupBySV(int length, @Nonnull int[] groupKeyArray,
      @Nonnull GroupByResultHolder groupByResultHolder, @Nonnull BlockValSet... blockValSets) {

    FieldSpec.DataType valueType = blockValSets[0].getValueType();
    switch (valueType) {
      case INT:
        int[] intValues = blockValSets[0].getIntValuesSV();
        for (int i = 0; i < length; i++) {
          setValueForGroupKey(groupByResultHolder, groupKeyArray[i], intValues[i]);
        }
        break;

      case LONG:
        long[] longValues = blockValSets[0].getLongValuesSV();
        for (int i = 0; i < length; i++) {
          setValueForGroupKey(groupByResultHolder, groupKeyArray[i], Long.valueOf(longValues[i]).hashCode());
        }
        break;

      case FLOAT:
        float[] floatValues = blockValSets[0].getFloatValuesSV();
        for (int i = 0; i < length; i++) {
          setValueForGroupKey(groupByResultHolder, groupKeyArray[i], Float.valueOf(floatValues[i]).hashCode());
        }
        break;

      case DOUBLE:
        double[] doubleValues = blockValSets[0].getDoubleValuesSV();
        for (int i = 0; i < length; i++) {
          int groupKey = groupKeyArray[i];
          setValueForGroupKey(groupByResultHolder, groupKey, Double.valueOf(doubleValues[i]).hashCode());
        }
        break;

      case STRING:
        String[] stringValues = blockValSets[0].getStringValuesSV();
        for (int i = 0; i < length; i++) {
          int groupKey = groupKeyArray[i];
          setValueForGroupKey(groupByResultHolder, groupKey, stringValues[i].hashCode());
        }
        break;

      default:
        throw new IllegalArgumentException("Illegal data type for distinct count aggregation function: " + valueType);
    }
  }

  @Override
  public void aggregateGroupByMV(int length, @Nonnull int[][] groupKeysArray,
      @Nonnull GroupByResultHolder groupByResultHolder, @Nonnull BlockValSet... blockValSets) {

    FieldSpec.DataType valueType = blockValSets[0].getValueType();
    switch (valueType) {
      case INT:
        int[] intValues = blockValSets[0].getIntValuesSV();
        for (int i = 0; i < length; i++) {
          setValueForGroupKeys(groupByResultHolder, groupKeysArray[i], intValues[i]);
        }
        break;

      case LONG:
        long[] longValues = blockValSets[0].getLongValuesSV();
        for (int i = 0; i < length; i++) {
          setValueForGroupKeys(groupByResultHolder, groupKeysArray[i], Long.valueOf(longValues[i]).hashCode());
        }
        break;

      case FLOAT:
        float[] floatValues = blockValSets[0].getFloatValuesSV();
        for (int i = 0; i < length; i++) {
          setValueForGroupKeys(groupByResultHolder, groupKeysArray[i], Float.valueOf(floatValues[i]).hashCode());
        }
        break;

      case DOUBLE:
        double[] doubleValues = blockValSets[0].getDoubleValuesSV();
        for (int i = 0; i < length; i++) {
          setValueForGroupKeys(groupByResultHolder, groupKeysArray[i], Double.valueOf(doubleValues[i]).hashCode());
        }
        break;

      case STRING:
        String[] singleValues = blockValSets[0].getStringValuesSV();
        for (int i = 0; i < length; i++) {
          int hashCode = singleValues[i].hashCode();
          for (int groupKey : groupKeysArray[i]) {
            setValueForGroupKey(groupByResultHolder, groupKey, hashCode);
          }
        }
        break;

      default:
        throw new IllegalArgumentException("Illegal data type for distinct count aggregation function: " + valueType);
    }
  }

  @Nonnull
  @Override
  public HyperLogLog extractAggregationResult(@Nonnull AggregationResultHolder aggregationResultHolder) {
    HyperLogLog hyperLogLog = aggregationResultHolder.getResult();
    if (hyperLogLog == null) {
      return new HyperLogLog(HllConstants.DEFAULT_LOG2M);
    } else {
      return hyperLogLog;
    }
  }

  @Nonnull
  @Override
  public HyperLogLog extractGroupByResult(@Nonnull GroupByResultHolder groupByResultHolder, int groupKey) {
    HyperLogLog hyperLogLog = groupByResultHolder.getResult(groupKey);
    if (hyperLogLog == null) {
      return new HyperLogLog(HllConstants.DEFAULT_LOG2M);
    } else {
      return hyperLogLog;
    }
  }

  @Nonnull
  @Override
  public HyperLogLog merge(@Nonnull HyperLogLog intermediateResult1, @Nonnull HyperLogLog intermediateResult2) {
    try {
      intermediateResult1.addAll(intermediateResult2);
    } catch (CardinalityMergeException e) {
      throw new RuntimeException("Caught exception while merging HyperLogLog.", e);
    }
    return intermediateResult1;
  }

  @Override
  public boolean isIntermediateResultComparable() {
    return false;
  }

  @Nonnull
  @Override
  public FieldSpec.DataType getIntermediateResultDataType() {
    return FieldSpec.DataType.OBJECT;
  }

  @Nonnull
  @Override
  public Long extractFinalResult(@Nonnull HyperLogLog intermediateResult) {
    return intermediateResult.cardinality();
  }

  /**
   * Helper method to set value for a groupKey into the result holder.
   *
   * @param groupByResultHolder Result holder
   * @param groupKey Group-key for which to set the value
   * @param value Value for the group key
   */
  private void setValueForGroupKey(@Nonnull GroupByResultHolder groupByResultHolder, int groupKey, int value) {
    HyperLogLog hyperLogLog = groupByResultHolder.getResult(groupKey);
    if (hyperLogLog == null) {
      hyperLogLog = new HyperLogLog(HllConstants.DEFAULT_LOG2M);
      groupByResultHolder.setValueForKey(groupKey, hyperLogLog);
    }
    hyperLogLog.offer(value);
  }

  /**
   * Helper method to set value for a given array of group keys.
   * @param groupByResultHolder Result Holder
   * @param groupKeys Group keys for which to set the value
   * @param value Value to set
   */
  private void setValueForGroupKeys(@Nonnull GroupByResultHolder groupByResultHolder, int[] groupKeys, int value) {
    for (int groupKey : groupKeys) {
      setValueForGroupKey(groupByResultHolder, groupKey, value);
    }
  }
}
