package com.linkedin.thirdeye.rootcause.impl;

import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;
import com.linkedin.thirdeye.rootcause.Entity;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;


/**
 * MetricEntity represents an individual metric. It holds meta-data referencing ThirdEye's internal
 * database. The URN namespace is defined as 'thirdeye:metric:{id}'.
 */
public class MetricEntity extends Entity {
  public static final EntityType TYPE = new EntityType("thirdeye:metric:");

  private final long id;
  private final Multimap<String, String> filters;

  protected MetricEntity(String urn, double score, List<? extends Entity> related, long id, Multimap<String, String> filters) {
    super(urn, score, related);
    this.id = id;
    this.filters = filters;
  }

  public long getId() {
    return id;
  }

  public Multimap<String, String> getFilters() {
    return this.filters;
  }

  @Override
  public MetricEntity withScore(double score) {
    return new MetricEntity(this.getUrn(), score, this.getRelated(), this.id, this.filters);
  }

  @Override
  public MetricEntity withRelated(List<? extends Entity> related) {
    return new MetricEntity(this.getUrn(), this.getScore(), related, this.id, this.filters);
  }

  public MetricEntity withFilters(Multimap<String, String> filters) {
    return new MetricEntity(TYPE.formatURN(this.id, encodeFilters(filters)), this.getScore(), this.getRelated(), this.id, filters);
  }

  public MetricEntity withoutFilters() {
    return new MetricEntity(TYPE.formatURN(this.id), this.getScore(), this.getRelated(), this.id, filters);
  }

  public static MetricEntity fromMetric(double score, Collection<? extends Entity> related, long id, Multimap<String, String> filters) {
    return new MetricEntity(TYPE.formatURN(id, encodeFilters(filters)), score, new ArrayList<>(related), id, TreeMultimap.create(filters));
  }

  public static MetricEntity fromMetric(double score, Collection<? extends Entity> related, long id) {
    return fromMetric(score, related, id, TreeMultimap.<String, String>create());
  }

  public static MetricEntity fromMetric(double score, long id, Multimap<String, String> filters) {
    return fromMetric(score, new ArrayList<Entity>(), id, filters);
  }

  public static MetricEntity fromMetric(double score, long id) {
    return fromMetric(score, new ArrayList<Entity>(), id, TreeMultimap.<String, String>create());
  }

  public static MetricEntity fromURN(String urn, double score) {
    if(!TYPE.isType(urn))
      throw new IllegalArgumentException(String.format("URN '%s' is not type '%s'", urn, TYPE.getPrefix()));
    // TODO handle filter strings containing ":"
    String[] parts = urn.split(":");
    if(parts.length <= 2)
      throw new IllegalArgumentException(String.format("URN must have at least 3 parts but has '%d'", parts.length));
    List<String> filterStrings = Arrays.asList(Arrays.copyOfRange(parts, 3, parts.length));
    return fromMetric(score, Long.parseLong(parts[2]), decodeFilters(filterStrings));
  }

  private static Multimap<String, String> decodeFilters(List<String> filterStrings) {
    Multimap<String, String> filters = TreeMultimap.create();

    for(String filterString : filterStrings) {
      String[] parts = EntityUtils.decodeURNComponent(filterString).split("=", 2);
      if (parts.length != 2) {
        throw new IllegalArgumentException(String.format("Could not parse filter string '%s'", filterString));
      }
      filters.put(parts[0], parts[1]);
    }

    return filters;
  }

  private static List<String> encodeFilters(Multimap<String, String> filters) {
    List<String> output = new ArrayList<>();

    Multimap<String, String> sorted = TreeMultimap.create(filters);
    for(Map.Entry<String, String> entry : sorted.entries()) {
      output.add(EntityUtils.encodeURNComponent(String.format("%s=%s", entry.getKey(), entry.getValue())));
    }

    return output;
  }

}
