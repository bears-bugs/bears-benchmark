package com.linkedin.thirdeye.datasource.pinot;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import java.util.Objects;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang3.StringUtils;

import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;
import com.linkedin.thirdeye.datasource.DataSourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An immutable configurations for setting up {@link PinotThirdEyeDataSource}'s connection to Pinot.
 */
public class PinotThirdEyeDataSourceConfig {
  private static final Logger LOG = LoggerFactory.getLogger(PinotThirdEyeDataSourceConfig.class);

  public static final String HTTP_SCHEME = "http";
  public static final String HTTPS_SCHEME = "https";

  private String zookeeperUrl;
  private String controllerHost;
  private int controllerPort;
  private String controllerConnectionScheme;
  private String clusterName;
  private String brokerUrl;
  private String tag;

  public String getZookeeperUrl() {
    return zookeeperUrl;
  }

  private void setZookeeperUrl(String zookeeperUrl) {
    this.zookeeperUrl = zookeeperUrl;
  }

  public String getControllerHost() {
    return controllerHost;
  }

  private void setControllerHost(String controllerHost) {
    this.controllerHost = controllerHost;
  }

  public int getControllerPort() {
    return controllerPort;
  }

  private void setControllerPort(int controllerPort) {
    this.controllerPort = controllerPort;
  }

  public String getTag() {
    return tag;
  }

  private void setTag(String tag) {
    this.tag = tag;
  }

  public String getBrokerUrl() {
    return brokerUrl;
  }

  private void setBrokerUrl(String brokerUrl) {
    this.brokerUrl = brokerUrl;
  }

  public String getClusterName() {
    return clusterName;
  }

  private void setClusterName(String clusterName) {
    this.clusterName = clusterName;
  }

  public String getControllerConnectionScheme() {
    return controllerConnectionScheme;
  }

  private void setControllerConnectionScheme(String controllerConnectionScheme) {
    this.controllerConnectionScheme = controllerConnectionScheme;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PinotThirdEyeDataSourceConfig config = (PinotThirdEyeDataSourceConfig) o;
    return getControllerPort() == config.getControllerPort() && Objects
        .equals(getZookeeperUrl(), config.getZookeeperUrl()) && Objects
        .equals(getControllerHost(), config.getControllerHost()) && Objects
        .equals(getControllerConnectionScheme(), config.getControllerConnectionScheme()) && Objects
        .equals(getClusterName(), config.getClusterName()) && Objects.equals(getBrokerUrl(), config.getBrokerUrl())
        && Objects.equals(getTag(), config.getTag());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getZookeeperUrl(), getControllerHost(), getControllerPort(), getControllerConnectionScheme(),
        getClusterName(), getBrokerUrl(), getTag());
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("zookeeperUrl", zookeeperUrl).add("controllerHost", controllerHost)
        .add("controllerPort", controllerPort).add("controllerConnectionScheme", controllerConnectionScheme)
        .add("clusterName", clusterName).add("brokerUrl", brokerUrl).add("tag", tag).toString();
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private String zookeeperUrl;
    private String controllerHost;
    private int controllerPort = -1;
    private String controllerConnectionScheme = HTTP_SCHEME; // HTTP_SCHEME or HTTPS_SCHEME
    private String clusterName;
    private String brokerUrl;
    private String tag;

    public Builder setZookeeperUrl(String zookeeperUrl) {
      this.zookeeperUrl = zookeeperUrl;
      return this;
    }

    public Builder setControllerHost(String controllerHost) {
      this.controllerHost = controllerHost;
      return this;
    }

    public Builder setControllerPort(int controllerPort) {
      this.controllerPort = controllerPort;
      return this;
    }

    public Builder setClusterName(String clusterName) {
      this.clusterName = clusterName;
      return this;
    }

    public Builder setBrokerUrl(String brokerUrl) {
      this.brokerUrl = brokerUrl;
      return this;
    }

    public Builder setTag(String tag) {
      this.tag = tag;
      return this;
    }

    public Builder setControllerConnectionScheme(String controllerConnectionScheme) {
      this.controllerConnectionScheme = controllerConnectionScheme;
      return this;
    }

    public PinotThirdEyeDataSourceConfig build() {
      final String className = PinotThirdEyeDataSourceConfig.class.getSimpleName();
      Preconditions.checkNotNull(controllerHost, "{} is missing 'Controller Host' property", className);
      Preconditions.checkArgument(controllerPort >= 0, "{} is missing 'Controller Port' property", className);
      Preconditions.checkNotNull(zookeeperUrl, "{} is missing 'Zookeeper URL' property", className);
      Preconditions.checkNotNull(clusterName, "{} is missing 'Cluster Name' property", className);
      Preconditions.checkArgument(
          controllerConnectionScheme.equals(HTTP_SCHEME) || controllerConnectionScheme.equals(HTTPS_SCHEME),
          "{} accepts only 'http' or 'https' connection schemes", className);

      PinotThirdEyeDataSourceConfig config = new PinotThirdEyeDataSourceConfig();
      config.setControllerHost(controllerHost);
      config.setControllerPort(controllerPort);
      config.setZookeeperUrl(zookeeperUrl);
      config.setClusterName(clusterName);
      config.setBrokerUrl(brokerUrl);
      config.setTag(tag);
      config.setControllerConnectionScheme(controllerConnectionScheme);
      return config;
    }
  }

  /**
   * Returns pinot thirdeye datasource config given datasource config. There can be only ONE datasource of pinot type
   *
   * @param dataSourceConfig
   *
   * @return
   */
  public static PinotThirdEyeDataSourceConfig createFromDataSourceConfig(DataSourceConfig dataSourceConfig) {
    if (dataSourceConfig == null || !dataSourceConfig.getClassName()
        .equals(PinotThirdEyeDataSource.class.getCanonicalName())) {
      throw new IllegalStateException("Data source config is not of type pinot " + dataSourceConfig);
    }
    return createFromProperties(dataSourceConfig.getProperties());
  }

  /**
   * Returns PinotThirdEyeDataSourceConfig from the given property map.
   *
   * @param properties the properties to setup a PinotThirdEyeDataSourceConfig.
   *
   * @return a PinotThirdEyeDataSourceConfig.
   *
   * @throws IllegalArgumentException is thrown if the property map does not contain all necessary fields, i.e.,
   *                                  controller host and port, cluster name, and the URL to zoo keeper.
   */
  static PinotThirdEyeDataSourceConfig createFromProperties(Map<String, String> properties) {
    ImmutableMap<String, String> processedProperties = processPropertyMap(properties);
    if (processedProperties == null) {
      throw new IllegalArgumentException(
          "Invalid properties for data source: " + PinotThirdEyeDataSource.DATA_SOURCE_NAME + ", properties="
              + properties);
    }

    String controllerHost = processedProperties.get(PinotThirdeyeDataSourceProperties.CONTROLLER_HOST.getValue());
    int controllerPort =
        Integer.valueOf(processedProperties.get(PinotThirdeyeDataSourceProperties.CONTROLLER_PORT.getValue()));
    String controllerConnectionScheme =
        processedProperties.get(PinotThirdeyeDataSourceProperties.CONTROLLER_CONNECTION_SCHEME.getValue());
    String zookeeperUrl = processedProperties.get(PinotThirdeyeDataSourceProperties.ZOOKEEPER_URL.getValue());
    String clusterName = processedProperties.get(PinotThirdeyeDataSourceProperties.CLUSTER_NAME.getValue());
    // brokerUrl and tag are optional
    String brokerUrl = processedProperties.get(PinotThirdeyeDataSourceProperties.BROKER_URL.getValue());
    String tag = processedProperties.get(PinotThirdeyeDataSourceProperties.TAG.getValue());

    Builder builder =
        PinotThirdEyeDataSourceConfig.builder().setControllerHost(controllerHost).setControllerPort(controllerPort)
            .setZookeeperUrl(zookeeperUrl).setClusterName(clusterName);
    if (StringUtils.isNotBlank(brokerUrl)) {
      builder.setBrokerUrl(brokerUrl);
    }
    if (StringUtils.isNotBlank(tag)) {
      builder.setTag(tag);
    }
    if (StringUtils.isNotBlank(controllerConnectionScheme)) {
      builder.setControllerConnectionScheme(controllerConnectionScheme);
    }

    return builder.build();
  }

  /**
   * Process the input properties and Checks if the given property map could be used to construct a
   * PinotThirdEyeDataSourceConfig. The essential fields are controller host and port, cluster name, and the URL to zoo
   * keeper. This method prints out all missing essential fields before returning a null processed map.
   *
   * @param properties the input properties to be checked.
   *
   * @return a processed property map; null if the given property map cannot be validated successfully.
   */
  static ImmutableMap<String, String> processPropertyMap(Map<String, String> properties) {
    if (MapUtils.isEmpty(properties)) {
      LOG.error("PinotThirdEyeDataSource is missing properties {}", properties);
      return null;
    }

    final List<PinotThirdeyeDataSourceProperties> requiredProperties = Arrays
        .asList(PinotThirdeyeDataSourceProperties.CONTROLLER_HOST, PinotThirdeyeDataSourceProperties.CONTROLLER_PORT,
            PinotThirdeyeDataSourceProperties.ZOOKEEPER_URL, PinotThirdeyeDataSourceProperties.CLUSTER_NAME);
    final List<PinotThirdeyeDataSourceProperties> optionalProperties = Arrays
        .asList(PinotThirdeyeDataSourceProperties.CONTROLLER_CONNECTION_SCHEME,
            PinotThirdeyeDataSourceProperties.BROKER_URL, PinotThirdeyeDataSourceProperties.TAG);

    // Validates required properties
    final String className = PinotControllerResponseCacheLoader.class.getSimpleName();
    boolean valid = true;
    ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
    for (PinotThirdeyeDataSourceProperties requiredProperty : requiredProperties) {
      String propertyString = Strings.nullToEmpty(properties.get(requiredProperty.getValue())).trim();
      if (Strings.isNullOrEmpty(propertyString)) {
        valid = false;
        LOG.error("{} is missing required property {}", className, requiredProperty);
      } else {
        builder.put(requiredProperty.getValue(), propertyString);
      }
    }

    if (valid) {
      // Copies optional properties
      for (PinotThirdeyeDataSourceProperties optionalProperty : optionalProperties) {
        String propertyString = Strings.nullToEmpty(properties.get(optionalProperty.getValue())).trim();
        if (!Strings.isNullOrEmpty(propertyString)) {
          builder.put(optionalProperty.getValue(), propertyString);
        }
      }

      return builder.build();
    } else {
      return null;
    }
  }
}
