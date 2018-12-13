package com.hubspot.baragon.config;

import javax.validation.constraints.NotNull;

public class ZooKeeperConfiguration {

  @NotNull
  private String quorum;
  @NotNull
  private Integer sessionTimeoutMillis;
  @NotNull
  private Integer connectTimeoutMillis;
  @NotNull
  private Integer retryBaseSleepTimeMilliseconds;
  @NotNull
  private Integer retryMaxTries;
  @NotNull
  private String zkNamespace;
  private long debugCuratorCallOverBytes = 25000;
  private long debugCuratorCallOverMillis = 250;

  public String getQuorum() {
    return quorum;
  }

  public void setQuorum(String quorum) {
    this.quorum = quorum;
  }

  public Integer getSessionTimeoutMillis() {
    return sessionTimeoutMillis;
  }

  public void setSessionTimeoutMillis(Integer sessionTimeoutMillis) {
    this.sessionTimeoutMillis = sessionTimeoutMillis;
  }

  public Integer getConnectTimeoutMillis() {
    return connectTimeoutMillis;
  }

  public void setConnectTimeoutMillis(Integer connectTimeoutMillis) {
    this.connectTimeoutMillis = connectTimeoutMillis;
  }

  public Integer getRetryBaseSleepTimeMilliseconds() {
    return retryBaseSleepTimeMilliseconds;
  }

  public void setRetryBaseSleepTimeMilliseconds(Integer retryBaseSleepTimeMilliseconds) {
    this.retryBaseSleepTimeMilliseconds = retryBaseSleepTimeMilliseconds;
  }

  public Integer getRetryMaxTries() {
    return retryMaxTries;
  }

  public void setRetryMaxTries(Integer retryMaxTries) {
    this.retryMaxTries = retryMaxTries;
  }

  public String getZkNamespace() {
    return zkNamespace;
  }

  public void setZkNamespace(String zkNamespace) {
    this.zkNamespace = zkNamespace;
  }

  public long getDebugCuratorCallOverBytes() {
    return debugCuratorCallOverBytes;
  }

  public void setDebugCuratorCallOverBytes(long debugCuratorCallOverBytes) {
    this.debugCuratorCallOverBytes = debugCuratorCallOverBytes;
  }

  public long getDebugCuratorCallOverMillis() {
    return debugCuratorCallOverMillis;
  }

  public void setDebugCuratorCallOverMillis(long debugCuratorCallOverMillis) {
    this.debugCuratorCallOverMillis = debugCuratorCallOverMillis;
  }
}
