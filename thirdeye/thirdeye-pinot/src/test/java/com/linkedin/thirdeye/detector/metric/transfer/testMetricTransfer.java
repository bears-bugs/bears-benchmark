package com.linkedin.thirdeye.detector.metric.transfer;

import com.linkedin.thirdeye.api.MetricSchema;
import com.linkedin.thirdeye.api.MetricTimeSeries;
import com.linkedin.thirdeye.api.MetricType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.testng.annotations.Test;
import org.testng.Assert;


public class testMetricTransfer {

  @Test
  public void transfer(){

    // create a mock MetricTimeSeries
    List<String> names = new ArrayList<>(1);
    String mName = "metric0";
    names.add(0, mName);
    List<MetricType> types = Collections.nCopies(names.size(), MetricType.DOUBLE);
    MetricSchema metricSchema = new MetricSchema(names, types);
    MetricTimeSeries metrics = new MetricTimeSeries(metricSchema);
    // the last three values are current values; the rest values are baseline values
    double [] m0 = {1.0, 1.0, 1.0, 1.0, 1.0, 1.0};
    for (long i=0l; i<=5l; i++) {
      metrics.set(i, mName, 1.0);
    }

    // create a list of mock scaling factors
    ScalingFactor sf0 = new ScalingFactor(2l, 6l, 0.8);
    List<ScalingFactor> sfList0 = new ArrayList<>();
    sfList0.add(sf0);

    Properties properties = new Properties();
    properties.put(MetricTransfer.SEASONAL_SIZE, "3");
    properties.put(MetricTransfer.SEASONAL_UNIT, TimeUnit.MILLISECONDS.toString());
    properties.put(MetricTransfer.BASELINE_SEASONAL_PERIOD, "2"); // mistakenly set 2 on purpose

    MetricTransfer.rescaleMetric(metrics, 3, sfList0, mName, properties);
    double [] m1_expected = {0.8, 0.8, Double.NaN, 1.0, 1.0, 1.0};
    double [] m_actual = new double[6];
    for (int i=0; i<=5; i++) {
      m_actual[i]= metrics.getOrDefault(i, mName, 0).doubleValue();
    }
    Assert.assertEquals(m_actual, m1_expected);

    //should not affect
    sfList0.remove(0);
    ScalingFactor sf1 = new ScalingFactor(12l, 14l, 0.8);
    sfList0.add(sf1);
    MetricTransfer.rescaleMetric(metrics, 3, sfList0, mName, properties);
    for (int i=0; i<=5; i++) {
      m_actual[i]= metrics.getOrDefault(i, mName, 0).doubleValue();
    }
    Assert.assertEquals(m_actual, m1_expected);

  }

}
