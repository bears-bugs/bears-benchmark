package com.linkedin.thirdeye.dashboard.views;

import java.util.List;

import org.joda.time.DateTime;

import com.google.common.collect.Multimap;
import com.linkedin.thirdeye.api.TimeGranularity;
import com.linkedin.thirdeye.datasource.MetricExpression;

public class CompareViewRequest implements ViewRequest {

  String collection;
  List<MetricExpression> metricExpressions;
  DateTime baselineStart;
  DateTime baselineEnd;
  DateTime currentStart;
  DateTime currentEnd;
  Multimap<String, String> filters;
  TimeGranularity timeGranularity;
  String compareMode;
  List<String> groupByDimensions;
  
  public CompareViewRequest() {
  }

  @Override
  public String getCollection() {
    return collection;
  }

  public void setCollection(String collection) {
    this.collection = collection;
  }

  @Override
  public Multimap<String, String> getFilters() {
    return filters;
  }

  public void setFilters(Multimap<String, String> filters) {
    this.filters = filters;
  }

  @Override
  public TimeGranularity getTimeGranularity() {
    return timeGranularity;
  }

  public void setTimeGranularity(TimeGranularity timeGranularity) {
    this.timeGranularity = timeGranularity;
  }

  public DateTime getBaselineStart() {
    return baselineStart;
  }

  public void setBaselineStart(DateTime baselineStart) {
    this.baselineStart = baselineStart;
  }

  public DateTime getBaselineEnd() {
    return baselineEnd;
  }

  public void setBaselineEnd(DateTime baselineEnd) {
    this.baselineEnd = baselineEnd;
  }

  public DateTime getCurrentStart() {
    return currentStart;
  }

  public void setCurrentStart(DateTime currentStart) {
    this.currentStart = currentStart;
  }

  public DateTime getCurrentEnd() {
    return currentEnd;
  }

  public void setCurrentEnd(DateTime currentEnd) {
    this.currentEnd = currentEnd;
  }

  public List<MetricExpression> getMetricExpressions() {
    return metricExpressions;
  }

  public void setMetricExpressions(List<MetricExpression> metricExpressions) {
    this.metricExpressions = metricExpressions;
  }

  public void setCompareMode(String compareMode) {
    this.compareMode = compareMode;
  }

  public String getCompareMode() {
    return compareMode;
  }
  public void setGroupByDimensions(List<String> groupByDimensions) {
    this.groupByDimensions = groupByDimensions;
  }
  
  public List<String> getGroupByDimensions() {
    return groupByDimensions;
  }

}
