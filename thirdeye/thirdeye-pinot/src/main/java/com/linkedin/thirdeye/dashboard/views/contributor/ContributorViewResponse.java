package com.linkedin.thirdeye.dashboard.views.contributor;

import java.util.List;
import java.util.Map;

import com.linkedin.thirdeye.dashboard.views.GenericResponse;
import com.linkedin.thirdeye.dashboard.views.TimeBucket;
import com.linkedin.thirdeye.dashboard.views.ViewResponse;

public class ContributorViewResponse implements ViewResponse {
  List<String> metrics;
  List<String> dimensions;
  Map<String, List<String>> dimensionValuesMap;
  List<TimeBucket> timeBuckets;
  Map<String, String> summary;
  GenericResponse responseData;
  // metrics vs dimension vs subDimension, aggValue
  Map<String, Map<String, Map<String, Double>>> baselineTotalMapPerDimensionValue;
  Map<String, Map<String, Map<String, Double>>> currentTotalMapPerDimensionValue;

  public ContributorViewResponse() {
    super();
  }

  public List<String> getMetrics() {
    return metrics;
  }

  public void setMetrics(List<String> metrics) {
    this.metrics = metrics;
  }

  public List<String> getDimensions() {
    return dimensions;
  }

  public void setDimensions(List<String> dimensions) {
    this.dimensions = dimensions;
  }

  public Map<String, String> getSummary() {
    return summary;
  }

  public void setSummary(Map<String, String> summary) {
    this.summary = summary;
  }

  public List<TimeBucket> getTimeBuckets() {
    return timeBuckets;
  }

  public void setTimeBuckets(List<TimeBucket> timeBuckets) {
    this.timeBuckets = timeBuckets;
  }

  public void setResponseData(GenericResponse responseData) {
    this.responseData = responseData;
  }

  public GenericResponse getResponseData() {
    return responseData;
  }

  public Map<String, List<String>> getDimensionValuesMap() {
    return dimensionValuesMap;
  }

  public void setDimensionValuesMap(Map<String, List<String>> dimensionValuesMap) {
    this.dimensionValuesMap = dimensionValuesMap;
  }

  public Map<String, Map<String, Map<String, Double>>> getBaselineTotalMapPerDimensionValue() {
    return baselineTotalMapPerDimensionValue;
  }

  public void setBaselineTotalMapPerDimensionValue(
      Map<String, Map<String, Map<String, Double>>> baselineTotalMapPerDimensionValue) {
    this.baselineTotalMapPerDimensionValue = baselineTotalMapPerDimensionValue;
  }

  public Map<String, Map<String, Map<String, Double>>> getCurrentTotalMapPerDimensionValue() {
    return currentTotalMapPerDimensionValue;
  }

  public void setCurrentTotalMapPerDimensionValue(
      Map<String, Map<String, Map<String, Double>>> currentTotalMapPerDimensionValue) {
    this.currentTotalMapPerDimensionValue = currentTotalMapPerDimensionValue;
  }
}
