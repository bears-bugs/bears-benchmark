package com.linkedin.thirdeye.datasource.timeseries;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.joda.time.DateTime;

import com.google.common.collect.Lists;
import com.linkedin.thirdeye.api.TimeGranularity;
import com.linkedin.thirdeye.constant.MetricAggFunction;
import com.linkedin.thirdeye.dashboard.Utils;
import com.linkedin.thirdeye.datasource.MetricExpression;
import com.linkedin.thirdeye.datasource.MetricFunction;
import com.linkedin.thirdeye.datasource.ThirdEyeDataSource;
import com.linkedin.thirdeye.datasource.cache.QueryCache;
import com.linkedin.thirdeye.datasource.pinot.PinotThirdEyeDataSource;
import com.linkedin.thirdeye.datasource.timeseries.TimeSeriesHandler;
import com.linkedin.thirdeye.datasource.timeseries.TimeSeriesRequest;
import com.linkedin.thirdeye.datasource.timeseries.TimeSeriesResponse;
import com.linkedin.thirdeye.datasource.timeseries.TimeSeriesRow.TimeSeriesMetric;

/** Manual test for verifying code works as expected (ie without exceptions thrown) */
public class TimeSeriesTest {
  private static final ArrayList<String> ABOOK_DIMENSIONS =
      Lists.newArrayList("browserName", "contactsOrigin", "deviceName", "continent", "countryCode",
          "environment", "locale", "osName", "pageKey", "source", "sourceApp");
  private static final String THIRDEYE_ABOOK = "thirdeyeAbook";
  private static final String COUNT = "__COUNT";
  private static final MetricFunction DEFAULT_METRIC_FUNCTION =
      new MetricFunction(MetricAggFunction.SUM, COUNT, 0L, THIRDEYE_ABOOK, null, null);
  private static final MetricExpression SUBMIT_RATE_EXPRESSION =
      new MetricExpression("submit_rate", "submits/impressions");
  private static final DateTime START = new DateTime(2016, 4, 1, 00, 00);

  public static void main(String[] args) throws Exception {
    URL resource = null;// = System.class.get.getResource("log4j.properties");
    if (resource == null) {
      resource = TimeSeriesHandler.class.getClassLoader().getResource("logback.x");
    }
    PinotThirdEyeDataSource pinotThirdEyeDataSource = PinotThirdEyeDataSource.getDefaultTestDataSource(); // TODO
                                                                                          // make
                                                                                          // this
    // configurable
    Map<String, ThirdEyeDataSource> dataSourceMap = new HashMap<>();
    dataSourceMap.put(PinotThirdEyeDataSource.class.getSimpleName(), pinotThirdEyeDataSource);

    QueryCache queryCache = new QueryCache(dataSourceMap, Executors.newFixedThreadPool(10));
    TimeSeriesRequest[] requests = new TimeSeriesRequest[] {
        generateGroupByTimeRequest(),
        // generateGroupByDimensionRequest(),
        // generateGroupByTimeAndDimension()
    };
    for (TimeSeriesRequest timeSeriesRequest : requests) {
      try {
        TimeSeriesHandler handler = new TimeSeriesHandler(queryCache);
        long start = System.currentTimeMillis();
        TimeSeriesResponse response = handler.handle(timeSeriesRequest);
        long end = System.currentTimeMillis();
        System.out.println("Time taken:" + (end - start));
        for (TimeSeriesMetric metric : response.getRow(0).getMetrics()) {
          System.out.print(metric.getMetricName() + "\t\t");
        }
        System.out.println();
        for (int i = 0; i < response.getNumRows(); i++) {
          System.out.println(response.getRow(i));
        }
      } catch (Exception e) {
        System.out.println("Request failed: " + timeSeriesRequest);
        e.printStackTrace();
        System.exit(-1);
      }
    }
    System.out.println(
        "No exceptions encountered during testing... but you still need to check for data quality!");
    System.exit(0);
  }

  private static TimeSeriesRequest generateGroupByTimeRequest() {
    TimeSeriesRequest timeSeriesRequest = new TimeSeriesRequest();
    timeSeriesRequest.setCollectionName(THIRDEYE_ABOOK);

    timeSeriesRequest.setStart(START);
    timeSeriesRequest.setEnd(START.plusDays(1));

    List<MetricFunction> metricFunctions = new ArrayList<>();
    metricFunctions.add(DEFAULT_METRIC_FUNCTION);
    List<MetricExpression> metricExpressions = Utils.convertToMetricExpressions(metricFunctions);
    metricExpressions.add(SUBMIT_RATE_EXPRESSION);
    timeSeriesRequest.setMetricExpressions(metricExpressions);
    timeSeriesRequest.setAggregationTimeGranularity(new TimeGranularity(1, TimeUnit.HOURS));
    return timeSeriesRequest;
  }

  private static TimeSeriesRequest generateGroupByDimensionRequest() {
    TimeSeriesRequest timeSeriesRequest = new TimeSeriesRequest();
    timeSeriesRequest.setCollectionName(THIRDEYE_ABOOK);
    timeSeriesRequest.setStart(START);
    timeSeriesRequest.setEnd(START.plusHours(3));

    timeSeriesRequest.setGroupByDimensions(ABOOK_DIMENSIONS);
    List<MetricFunction> metricFunctions = new ArrayList<>();
    metricFunctions.add(DEFAULT_METRIC_FUNCTION);
    List<MetricExpression> metricExpressions = Utils.convertToMetricExpressions(metricFunctions);
    timeSeriesRequest.setMetricExpressions(metricExpressions);
    timeSeriesRequest.setAggregationTimeGranularity(null);
    return timeSeriesRequest;
  }

  private static TimeSeriesRequest generateGroupByTimeAndDimension() {
    TimeSeriesRequest timeSeriesRequest = new TimeSeriesRequest();
    timeSeriesRequest.setCollectionName(THIRDEYE_ABOOK);
    timeSeriesRequest.setStart(START);
    timeSeriesRequest.setEnd(START.plusHours(3));
    timeSeriesRequest.setGroupByDimensions(ABOOK_DIMENSIONS);
    List<MetricFunction> metricFunctions = new ArrayList<>();
    metricFunctions.add(DEFAULT_METRIC_FUNCTION);
    List<MetricExpression> metricExpressions = Utils.convertToMetricExpressions(metricFunctions);
    timeSeriesRequest.setMetricExpressions(metricExpressions);
    timeSeriesRequest.setAggregationTimeGranularity(new TimeGranularity(1, TimeUnit.HOURS));
    return timeSeriesRequest;
  }
}
