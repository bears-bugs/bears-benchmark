package com.hubspot.baragon.agent.managed;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.hubspot.baragon.agent.config.BaragonAgentConfiguration;
import com.hubspot.baragon.config.GraphiteConfiguration;
import com.hubspot.baragon.utils.JavaUtils;

import io.dropwizard.lifecycle.Managed;

@Singleton
public class BaragonAgentGraphiteReporterManaged implements Managed {

  private static final Logger LOG = LoggerFactory.getLogger(BaragonAgentGraphiteReporterManaged.class);

  private final GraphiteConfiguration graphiteConfiguration;
  private final MetricRegistry registry;
  private Optional<GraphiteReporter> reporter;

  @Inject
  public BaragonAgentGraphiteReporterManaged(BaragonAgentConfiguration configuration, MetricRegistry registry) {
    this.graphiteConfiguration = configuration.getGraphiteConfiguration();
    this.registry = registry;
    this.reporter = Optional.absent();
  }

  @Override
  public void start() throws Exception {
    if (!graphiteConfiguration.isEnabled()) {
      LOG.info("Not reporting data points to graphite.");
      return;
    }

    LOG.info("Reporting data points to graphite server {}:{} every {} seconds with prefix '{}' and predicates '{}'.", graphiteConfiguration.getHostname(),
        graphiteConfiguration.getPort(), graphiteConfiguration.getPeriodSeconds(), graphiteConfiguration.getPrefix(), JavaUtils.COMMA_JOINER.join(graphiteConfiguration.getPredicates()));

    final Graphite graphite = new Graphite(new InetSocketAddress(graphiteConfiguration.getHostname(), graphiteConfiguration.getPort()));

    final GraphiteReporter.Builder reporterBuilder = GraphiteReporter.forRegistry(registry);

    if (!Strings.isNullOrEmpty(graphiteConfiguration.getPrefix())) {
      reporterBuilder.prefixedWith(graphiteConfiguration.getPrefix());
    }

    if (!graphiteConfiguration.getPredicates().isEmpty()) {
      reporterBuilder.filter(new MetricFilter() {
        @Override
        public boolean matches(String name, Metric metric) {
          for (String predicate : graphiteConfiguration.getPredicates()) {
            if (name.startsWith(predicate)) {
              return true;
            }
          }
          return false;
        }
      });
    }

    reporter = Optional.of(reporterBuilder.build(graphite));
    reporter.get().start(graphiteConfiguration.getPeriodSeconds(), TimeUnit.SECONDS);
  }

  @Override
  public void stop() throws Exception {
    if (reporter.isPresent()) {
      reporter.get().stop();
    }
  }

}
