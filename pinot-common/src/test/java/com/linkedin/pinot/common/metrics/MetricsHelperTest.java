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
package com.linkedin.pinot.common.metrics;

import com.yammer.metrics.core.MetricName;
import com.yammer.metrics.core.MetricsRegistry;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.MapConfiguration;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;


/**
 * Tests for the MetricsHelper class.
 *
 */
public class MetricsHelperTest {
  public static boolean listenerOneOkay;
  public static boolean listenerTwoOkay;

  public static class ListenerOne implements MetricsRegistryRegistrationListener {
    @Override
    public void onMetricsRegistryRegistered(MetricsRegistry metricsRegistry) {
      listenerOneOkay = true;
    }
  }

  public static class ListenerTwo implements MetricsRegistryRegistrationListener {
    @Override
    public void onMetricsRegistryRegistered(MetricsRegistry metricsRegistry) {
      listenerTwoOkay = true;
    }
  }

  @Test
  public void testMetricsHelperRegistration() {
    listenerOneOkay = false;
    listenerTwoOkay = false;

    Map<String, String> configKeys = new HashMap<String, String>();
    configKeys.put("pinot.broker.metrics.metricsRegistryRegistrationListeners",
        ListenerOne.class.getName() + "," + ListenerTwo.class.getName());
    Configuration configuration = new MapConfiguration(configKeys);

    MetricsRegistry registry = new MetricsRegistry();

    // Initialize the MetricsHelper and create a new timer
    MetricsHelper.initializeMetrics(configuration.subset("pinot.broker.metrics"));
    MetricsHelper.registerMetricsRegistry(registry);
    MetricsHelper.newTimer(registry, new MetricName(MetricsHelperTest.class, "dummy"), TimeUnit.MILLISECONDS, TimeUnit.MILLISECONDS);

    // Check that the two listeners fired
    assertTrue(listenerOneOkay);
    assertTrue(listenerTwoOkay);
  }
}
