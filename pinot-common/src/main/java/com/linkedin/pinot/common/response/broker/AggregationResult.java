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
package com.linkedin.pinot.common.response.broker;

import java.io.Serializable;
import java.util.List;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;


/**
 * This class models the aggregation and aggregationGroupBy sections of query response.
 */
@JsonPropertyOrder({"groupByResult", "function", "groupByColumns"})
public class AggregationResult {

  private Serializable _value;
  private String _function;

  List<GroupByResult> _groupByResults;
  List<String> _groupByColumns;

  /**
   * Default constructor, required by JSON de-serializer.
   */
  public AggregationResult() {
  }

  /**
   * Constructor for aggregation response.
   * @param function
   * @param value
   */
  public AggregationResult(String function, Serializable value) {
    _function = function;
    _value = value;
    _groupByResults = null;
  }

  /**
   * Constructor for aggregationGroupByResponse.
   * @param group
   * @param groupByColumns
   * @param function
   */
  public AggregationResult(List<GroupByResult> group, List<String> groupByColumns, String function) {
    _groupByResults = group;
    _groupByColumns = groupByColumns;
    _function = function;
    _value = null;
  }

  /**
   * Returns the aggregation function name.
   * @return
   */
  @JsonProperty("function")
  @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
  public String getFunction() {
    return _function;
  }

  /**
   * Set the aggregation function name.
   * @param function
   */
  @JsonProperty("function")
  public void setFunction(String function) {
    _function = function;
  }

  /**
   * Returns the aggregation function value.
   * @return
   */
  @JsonProperty("value")
  @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
  public Serializable getValue() {
    return _value;
  }

  /**
   * Set the aggregation function value.
   * @param value
   */
  @JsonProperty("value")
  public void setValue(Serializable value) {
    _value = value;
  }

  /**
   * Set groupByResults for the aggregation function.
   * @return
   */
  @JsonProperty("groupByResult")
  @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
  public List<GroupByResult> getGroupByResult() {
    return _groupByResults;
  }

  /**
   * Get groupByResults for the aggregation function.
   * @param groupByResult
   */
  @JsonProperty("groupByResult")
  public void setGroupByResult(List<GroupByResult> groupByResult) {
    _groupByResults = groupByResult;
  }

  /**
   * Get groupByColumns for the aggregation function.
   * @return
   */
  @JsonProperty("groupByColumns")
  @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
  public List<String> getGroupByColumns() {
    return _groupByColumns;
  }

  /**
   * Set groupByColumns for the aggregation function.
   * @param groupByColumns
   */
  @JsonProperty("groupByColumns")
  public void setGroupByColumns(List<String> groupByColumns) {
    _groupByColumns = groupByColumns;
  }
}
