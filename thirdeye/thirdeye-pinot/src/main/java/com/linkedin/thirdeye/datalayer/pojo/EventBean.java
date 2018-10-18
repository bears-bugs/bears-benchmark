package com.linkedin.thirdeye.datalayer.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@JsonIgnoreProperties(ignoreUnknown = true)
public class EventBean extends AbstractBean {
  String name;
  String metric;
  String service;
  String eventType;

  long startTime;
  long endTime;

  /**
   * targetDimesionMap will hold metadata of the event. for example holiday event will have
   * coutntryCode --> {US, CA, ...}
   * DeploymentEvent will have fabric --- > {prod-lva1, prod-ltx1..} hostname ---> {hosta, hostb,
   * hostc...}
   */
  Map<String, List<String>> targetDimensionMap;

  public String getMetric() {
    return metric;
  }

  public Map<String, List<String>> getTargetDimensionMap() {
    return targetDimensionMap;
  }

  public void setTargetDimensionMap(Map<String, List<String>> targetDimensionMap) {
    this.targetDimensionMap = targetDimensionMap;
  }

  public String getEventType() {
    return eventType;
  }

  public void setEventType(String eventType) {
    this.eventType = eventType;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setMetric(String metric) {
    this.metric = metric;
  }

  public String getService() {
    return service;
  }

  public void setService(String service) {
    this.service = service;
  }

  public long getEndTime() {
    return endTime;
  }

  public void setEndTime(long endTime) {
    this.endTime = endTime;
  }

  public long getStartTime() {
    return startTime;
  }

  public void setStartTime(long startTime) {
    this.startTime = startTime;
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), name, eventType, targetDimensionMap, service, metric, startTime, endTime);
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof EventBean)) {
      return false;
    }
    EventBean eb = (EventBean) obj;
    return Objects.equals(getId(), eb.getId()) && Objects.equals(getName(), eb.getName()) &&
        Objects.equals(getEventType(), eb.getEventType()) &&
        Objects.equals(getTargetDimensionMap(), eb.getTargetDimensionMap()) &&
        Objects.equals(getService(), eb.getService()) && Objects.equals(getMetric(), eb.getMetric()) &&
        Objects.equals(getStartTime(), eb.getStartTime()) && Objects.equals(getEndTime(), eb.getEndTime());
  }

}
