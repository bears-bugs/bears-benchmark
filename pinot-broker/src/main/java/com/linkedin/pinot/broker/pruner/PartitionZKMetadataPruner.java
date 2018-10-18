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
package com.linkedin.pinot.broker.pruner;

import com.linkedin.pinot.common.metadata.segment.ColumnPartitionMetadata;
import com.linkedin.pinot.common.metadata.segment.SegmentPartitionMetadata;
import com.linkedin.pinot.common.metadata.segment.SegmentZKMetadata;
import com.linkedin.pinot.common.request.FilterOperator;
import com.linkedin.pinot.common.utils.request.FilterQueryTree;
import com.linkedin.pinot.core.data.partition.PartitionFunction;
import com.linkedin.pinot.core.data.partition.PartitionFunctionFactory;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import org.apache.commons.lang.math.IntRange;


/**
 * Implementation of {@link SegmentZKMetadataPruner} that prunes segment based on
 * partition information:
 * <ul>
 *   <li> Walks the filter query tree and compares segment partition id against predicate for all partitioned columns.</li>
 *   <li> Prunes segment when its partition id cannot satisfy the predicate(s) in the query.</li>
 * </ul>
 */
public class PartitionZKMetadataPruner implements SegmentZKMetadataPruner {

  @Override
  public boolean prune(SegmentZKMetadata segmentZKMetadata, SegmentPrunerContext prunerContext) {
    SegmentPartitionMetadata partitionMetadata = segmentZKMetadata.getPartitionMetadata();
    if (partitionMetadata == null) {
      return false;
    }

    Map<String, ColumnPartitionMetadata> columnPartitionMap = partitionMetadata.getColumnPartitionMap();
    FilterQueryTree filterQueryTree = prunerContext.getFilterQueryTree();
    return pruneSegment(filterQueryTree, columnPartitionMap);
  }

  /**
   * Helper method to prune a segment based on the filter query tree and partition metadata.
   *
   * @param filterQueryTree Filter tree for the predicates in the query
   * @param columnMetadataMap Map of column name to partition metadata for the column.
   *
   * @return True if the segment can be pruned, false otherwise.
   */
  private boolean pruneSegment(FilterQueryTree filterQueryTree,
      Map<String, ColumnPartitionMetadata> columnMetadataMap) {
    if (filterQueryTree == null) {
      return false;
    }

    List<FilterQueryTree> children = filterQueryTree.getChildren();

    // Non-leaf node
    if (children != null && !children.isEmpty()) {
      return pruneNonLeaf(filterQueryTree, columnMetadataMap);
    }

    // TODO: Enhance partition based pruning for RANGE operator.
    if (filterQueryTree.getOperator() != FilterOperator.EQUALITY) {
      return false;
    }

    // Leaf node
    String column = filterQueryTree.getColumn();
    ColumnPartitionMetadata metadata = columnMetadataMap.get(column);
    if (metadata == null) {
      return false;
    }

    List<IntRange> partitionRanges = metadata.getPartitionRanges();
    if (partitionRanges == null || partitionRanges.isEmpty()) {
      return false;
    }

    String value = filterQueryTree.getValue().get(0);
    PartitionFunction partitionFunction =
        PartitionFunctionFactory.getPartitionFunction(metadata.getFunctionName(), metadata.getNumPartitions());
    int partition = partitionFunction.getPartition(value);

    for (IntRange partitionRange : partitionRanges) {
      if (partitionRange.containsInteger(partition)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Given a non leaf filter query tree node prunes it as follows:
   * <ul>
   *   <li> For 'AND', node is pruned as long as at least one child can prune it. </li>
   *   <li> For 'OR', node is pruned as long as all children can prune it. </li>
   * </ul>
   *
   * @param filterQueryTree Non leaf node in the filter query tree.
   * @param columnMetadataMap Map for column metadata.
   *
   * @return True to prune, false otherwise
   */
  @SuppressWarnings("Duplicates")
  private boolean pruneNonLeaf(@Nonnull FilterQueryTree filterQueryTree,
      @Nonnull Map<String, ColumnPartitionMetadata> columnMetadataMap) {
    List<FilterQueryTree> children = filterQueryTree.getChildren();

    if (children.isEmpty()) {
      return false;
    }

    FilterOperator filterOperator = filterQueryTree.getOperator();
    switch (filterOperator) {
      case AND:
        for (FilterQueryTree child : children) {
          if (pruneSegment(child, columnMetadataMap)) {
            return true;
          }
        }
        return false;

      case OR:
        for (FilterQueryTree child : children) {
          if (!pruneSegment(child, columnMetadataMap)) {
            return false;
          }
        }
        return true;

      default:
        throw new IllegalStateException("Unsupported filter operator: " + filterOperator);
    }
  }
}
