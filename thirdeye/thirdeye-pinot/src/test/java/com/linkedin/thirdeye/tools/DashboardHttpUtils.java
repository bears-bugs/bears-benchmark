package com.linkedin.thirdeye.tools;

import com.linkedin.thirdeye.anomaly.utils.AbstractResourceHttpUtils;
import com.linkedin.thirdeye.api.TimeGranularity;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.HttpGet;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;


public class DashboardHttpUtils extends AbstractResourceHttpUtils {
  private final String DEFAULT_PATH_TO_TIMESERIES = "/dashboard/data/timeseries?";
  private final String DATASET = "dataset";
  private final String METRIC = "metrics";
  private final String VIEW = "view";
  private final String DEFAULT_VIEW = "timeseries";
  private final String TIME_START = "currentStart";
  private final String TIME_END = "currentEnd";
  private final String GRANULARITY = "aggTimeGranularity";
  private final String DIMENSIONS = "dimensions"; // separate by comma
  private final String FILTERS = "filters";
  private final String EQUALS = "=";
  private final String AND = "&";

  public DashboardHttpUtils(String host, int port, String authToken) {
    super(new HttpHost(host, port));
    addAuthenticationCookie(authToken);
  }

  public String handleMetricViewRequest(String dataset, String metric, DateTime startTime, DateTime endTime,
      TimeUnit timeUnit, String dimensions, String filterJson, String timezone) throws Exception{
    DateTimeZone dateTimeZone = DateTimeZone.forID(timezone);
    startTime = new DateTime(startTime, dateTimeZone);
    endTime = new DateTime(endTime, dateTimeZone);
    // format http GET command
    StringBuilder urlBuilder = new StringBuilder(DEFAULT_PATH_TO_TIMESERIES);
    urlBuilder.append(DATASET + EQUALS + dataset + AND);
    urlBuilder.append(METRIC + EQUALS + metric + AND);
    urlBuilder.append(VIEW + EQUALS + DEFAULT_VIEW + AND);
    urlBuilder.append(TIME_START + EQUALS + Long.toString(startTime.getMillis()) + AND);
    urlBuilder.append(TIME_END + EQUALS + Long.toString(endTime.getMillis()) + AND);
    urlBuilder.append(GRANULARITY + EQUALS + timeUnit.name() + AND);
    if (dimensions != null && !dimensions.isEmpty()) {
      urlBuilder.append(DIMENSIONS + EQUALS + dimensions + AND);
    }
    if (filterJson != null && !filterJson.isEmpty()) {
      urlBuilder.append(FILTERS + EQUALS + URLEncoder.encode(filterJson, "UTF-8"));
    }

    HttpGet httpGet = new HttpGet(urlBuilder.toString());
    return callJobEndpoint(httpGet);
  }
}
