package com.linkedin.thirdeye.dashboard.resources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkedin.thirdeye.api.MetricType;
import com.linkedin.thirdeye.dashboard.Utils;
import com.linkedin.thirdeye.datalayer.bao.MetricConfigManager;
import com.linkedin.thirdeye.datalayer.dto.MetricConfigDTO;
import com.linkedin.thirdeye.datasource.DAORegistry;
import com.linkedin.thirdeye.util.JsonResponseUtil;
import com.linkedin.thirdeye.util.ThirdEyeUtils;

@Path(value = "/thirdeye-admin/metric-config")
@Produces(MediaType.APPLICATION_JSON)
public class MetricConfigResource {
  private static final Logger LOG = LoggerFactory.getLogger(MetricConfigResource.class);

  private static final DAORegistry DAO_REGISTRY = DAORegistry.getInstance();
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  private MetricConfigManager metricConfigDao;

  public MetricConfigResource() {
    this.metricConfigDao = DAO_REGISTRY.getMetricConfigDAO();
  }

  @GET
  @Path("/create")
  public String createMetricConfig(@QueryParam("dataset") String dataset, @QueryParam("name") String name, @QueryParam("datatype") String metricType,
      @QueryParam("active") boolean active, @QueryParam("derived") boolean derived, @QueryParam("derivedFunctionType") String derivedFunctionType,
      @QueryParam("numerator") String numerator, @QueryParam("denominator") String denominator,
      @QueryParam("derivedMetricExpression") String derivedMetricExpression, @QueryParam("inverseMetric") boolean inverseMetric,
      @QueryParam("cellSizeExpression") String cellSizeExpression, @QueryParam("rollupThreshold") Double rollupThreshold) {
    try {
      MetricConfigDTO metricConfigDTO = new MetricConfigDTO();
      populateMetricConfig(metricConfigDTO, dataset, name, metricType, active, derived, derivedFunctionType, numerator, denominator, derivedMetricExpression,
          inverseMetric, cellSizeExpression, rollupThreshold);
      Long id = metricConfigDao.save(metricConfigDTO);

      metricConfigDTO.setId(id);
      return JsonResponseUtil.buildResponseJSON(metricConfigDTO).toString();
    } catch (Exception e) {
      LOG.warn("Failed to create metric:{}", name, e);
      return JsonResponseUtil.buildErrorResponseJSON("Failed to create metric:" + name + " Message:" + e.getMessage()).toString();
    }
  }

  private void populateMetricConfig(MetricConfigDTO metricConfigDTO, String dataset, String name, String metricType, boolean active, boolean derived,
      String derivedFunctionType, String numerator, String denominator, String derivedMetricExpression, boolean inverseMetric, String cellSizeExpression,
      Double rollupThreshold) {
    metricConfigDTO.setDataset(dataset);
    metricConfigDTO.setName(name);
    metricConfigDTO.setAlias(ThirdEyeUtils.constructMetricAlias(dataset, name));
    metricConfigDTO.setDatatype(MetricType.valueOf(metricType));
    metricConfigDTO.setActive(active);

    // optional ones
    metricConfigDTO.setCellSizeExpression(cellSizeExpression);
    metricConfigDTO.setInverseMetric(inverseMetric);
    if (rollupThreshold != null) {
      metricConfigDTO.setRollupThreshold(rollupThreshold);
    }

    // handle derived
    if (derived) {
      if (StringUtils.isEmpty(derivedMetricExpression) && numerator != null && denominator != null) {
        MetricConfigDTO numMetricConfigDTO = metricConfigDao.findByAliasAndDataset(numerator, dataset);
        MetricConfigDTO denMetricConfigDTO = metricConfigDao.findByAliasAndDataset(denominator, dataset);
        if ("RATIO".equals(derivedFunctionType)) {
          derivedMetricExpression = String.format("id%s/id%s", numMetricConfigDTO.getId(), denMetricConfigDTO.getId());
        } else if ("PERCENT".equals(derivedFunctionType)) {
          derivedMetricExpression = String.format("id%s*100/id%s", numMetricConfigDTO.getId(), denMetricConfigDTO.getId());
        }
      }
      metricConfigDTO.setDerived(derived);
      metricConfigDTO.setDerivedMetricExpression(derivedMetricExpression);
    }
  }

  @GET
  @Path("/metrics")
  public String getMetricsForDataset(@NotNull @QueryParam("dataset") String dataset) {
    Map<String, Object> filters = new HashMap<>();
    filters.put("dataset", dataset);
    List<MetricConfigDTO> metricConfigDTOs = metricConfigDao.findByParams(filters);
    List<String> metrics = new ArrayList<>();
    for (MetricConfigDTO metricConfigDTO : metricConfigDTOs) {
      metrics.add(metricConfigDTO.getAlias());
    }
    return JsonResponseUtil.buildResponseJSON(metrics).toString();
  }

  @GET
  @Path("/update")
  public String updateMetricConfig(@NotNull @QueryParam("id") long metricConfigId, @QueryParam("dataset") String dataset, @QueryParam("name") String name,
      @QueryParam("datatype") String metricType, @QueryParam("active") boolean active, @QueryParam("derived") boolean derived,
      @QueryParam("derivedFunctionType") String derivedFunctionType, @QueryParam("numerator") String numerator, @QueryParam("denominator") String denominator,
      @QueryParam("derivedMetricExpression") String derivedMetricExpression, @QueryParam("inverseMetric") boolean inverseMetric,
      @QueryParam("cellSizeExpression") String cellSizeExpression, @QueryParam("rollupThreshold") Double rollupThreshold) {
    try {

      MetricConfigDTO metricConfigDTO = metricConfigDao.findById(metricConfigId);
      populateMetricConfig(metricConfigDTO, dataset, name, metricType, active, derived, derivedFunctionType, numerator, denominator, derivedMetricExpression,
          inverseMetric, cellSizeExpression, rollupThreshold);
      int numRowsUpdated = metricConfigDao.update(metricConfigDTO);
      if (numRowsUpdated == 1) {
        return JsonResponseUtil.buildResponseJSON(metricConfigDTO).toString();
      } else {
        return JsonResponseUtil.buildErrorResponseJSON("Failed to update metric id:" + metricConfigId).toString();
      }
    } catch (Exception e) {
      return JsonResponseUtil.buildErrorResponseJSON("Failed to update metric id:" + metricConfigId + ". Exception:" + e.getMessage()).toString();
    }
  }

  @GET
  @Path("/delete")
  public String deleteMetricConfig(@NotNull @QueryParam("dataset") String dataset, @NotNull @QueryParam("id") Long metricConfigId) {
    metricConfigDao.deleteById(metricConfigId);
    return JsonResponseUtil.buildSuccessResponseJSON("Successully deleted " + metricConfigId).toString();
  }

  @GET
  @Path("/list")
  @Produces(MediaType.APPLICATION_JSON)
  public String viewMetricConfig(@NotNull @QueryParam("dataset") String dataset, @DefaultValue("0") @QueryParam("jtStartIndex") int jtStartIndex,
      @DefaultValue("100") @QueryParam("jtPageSize") int jtPageSize) {
    Map<String, Object> filters = new HashMap<>();
    filters.put("dataset", dataset);
    List<MetricConfigDTO> metricConfigDTOs = metricConfigDao.findByParams(filters);
    Collections.sort(metricConfigDTOs, new Comparator<MetricConfigDTO>() {

      @Override
      public int compare(MetricConfigDTO m1, MetricConfigDTO m2) {
        return m1.getName().compareTo(m2.getName());
      }
    });
    List<MetricConfigDTO> subList = Utils.sublist(metricConfigDTOs, jtStartIndex, jtPageSize);
    ObjectNode rootNode = JsonResponseUtil.buildResponseJSON(subList);
    return rootNode.toString();
  }

  @GET
  @Path("/view")
  @Produces(MediaType.APPLICATION_JSON)
  public MetricConfigDTO viewMetricConfigByIdOrName(@QueryParam("id") String id,
      @QueryParam("dataset") String dataset,
      @QueryParam("metric") String metric) {

    MetricConfigDTO metricConfigDTO = null;
    if (StringUtils.isBlank(id) && (StringUtils.isBlank(dataset) || StringUtils.isBlank(metric))) {
      LOG.error("Must provide either id or metric+dataset {} {} {}", id, metric, dataset);
    }
    if (StringUtils.isNotBlank(id)) {
      metricConfigDTO = metricConfigDao.findById(Long.valueOf(id));
    } else {
      metricConfigDTO = metricConfigDao.findByMetricAndDataset(metric, dataset);
    }
    return metricConfigDTO;
  }

  @GET
  @Path("/view/dataset/{dataset}")
  @Produces(MediaType.APPLICATION_JSON)
  public List<MetricConfigDTO> findByDataset(@PathParam("dataset") String dataset) {
    List<MetricConfigDTO> metricConfigs = metricConfigDao.findByDataset(dataset);
    return metricConfigs;
  }


  @POST
  @Path("/create/payload")
  public Long createMetricConfig(String payload) {
    Long id = null;
    try {
      MetricConfigDTO metricConfig = OBJECT_MAPPER.readValue(payload, MetricConfigDTO.class);
      id = metricConfigDao.save(metricConfig);
    } catch (IOException e) {
      LOG.error("Exception in creating dataset config with payload {}", payload);
    }
    return id;
  }

  public Long createMetricConfig(MetricConfigDTO metricConfig) {
    Long id = metricConfigDao.save(metricConfig);
    return id;
  }

  public void updateMetricConfig(MetricConfigDTO metricConfig) {
    metricConfigDao.update(metricConfig);
  }

}
