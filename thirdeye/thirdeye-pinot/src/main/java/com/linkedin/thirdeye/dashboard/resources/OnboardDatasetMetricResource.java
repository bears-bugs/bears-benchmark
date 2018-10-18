package com.linkedin.thirdeye.dashboard.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkedin.thirdeye.datalayer.bao.OnboardDatasetMetricManager;
import com.linkedin.thirdeye.datalayer.dto.OnboardDatasetMetricDTO;
import com.linkedin.thirdeye.datasource.DAORegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.StringUtils;

/**
 * Endpoints for adding datasets and metrics to be read by data sources
 */
@Path(value = "/onboard")
@Produces(MediaType.APPLICATION_JSON)
public class OnboardDatasetMetricResource {

  private static ObjectMapper OBJECT_MAPPER = new ObjectMapper();
  private static final DAORegistry DAO_REGISTRY = DAORegistry.getInstance();
  private static final OnboardDatasetMetricManager onboardDatasetMetricDAO = DAO_REGISTRY.getOnboardDatasetMetricDAO();


  public OnboardDatasetMetricResource() {
  }

  @GET
  @Path("/view")
  @Produces(MediaType.APPLICATION_JSON)
  public List<OnboardDatasetMetricDTO> viewOnboardConfigs(
      @QueryParam("datasetName") String datasetName,
      @QueryParam("metricName") String metricName,
      @QueryParam("dataSource") String dataSource,
      @QueryParam("onboarded") Boolean onboarded) {

    List<OnboardDatasetMetricDTO> dtos = new ArrayList<>();
    if (StringUtils.isBlank(datasetName) && StringUtils.isBlank(metricName) && StringUtils.isBlank(dataSource)
        && onboarded == null) {
      dtos = onboardDatasetMetricDAO.findAll();
    } else {
      Map<String, Object> filters = new HashMap<>();
      if (StringUtils.isNotBlank(datasetName)) {
        filters.put("datasetName", datasetName);
      }
      if (StringUtils.isNotBlank(metricName)) {
        filters.put("metricName", metricName);
      }
      if (StringUtils.isNotBlank(dataSource)) {
        filters.put("dataSource", dataSource);
      }
      if (onboarded != null) {
        filters.put("onboarded", onboarded);
      }
      dtos = onboardDatasetMetricDAO.findByParams(filters);
    }
    return dtos;
  }


  @GET
  @Path("/view/all")
  @Produces(MediaType.APPLICATION_JSON)
  public List<OnboardDatasetMetricDTO> viewAllOnboardConfigs() {
    List<OnboardDatasetMetricDTO> configs = onboardDatasetMetricDAO.findAll();
    return configs;
  }

  @GET
  @Path("/view/datasetName/{datasetName}")
  @Produces(MediaType.APPLICATION_JSON)
  public List<OnboardDatasetMetricDTO> viewOnboardConfigsByDataset(@PathParam("datasetName") String datasetName) {
    List<OnboardDatasetMetricDTO> dtos = onboardDatasetMetricDAO.findByDataset(datasetName);
    return dtos;
  }


  @GET
  @Path("/view/metricName/{metricName}")
  @Produces(MediaType.APPLICATION_JSON)
  public List<OnboardDatasetMetricDTO> viewOnboardConfigsByMetric(@PathParam("metricName") String metricName) {
    List<OnboardDatasetMetricDTO> dtos = onboardDatasetMetricDAO.findByMetric(metricName);
    return dtos;
  }


  @GET
  @Path("/view/dataSource/{dataSource}")
  @Produces(MediaType.APPLICATION_JSON)
  public List<OnboardDatasetMetricDTO> viewOnboardConfigsByDatasource(@PathParam("dataSource") String dataSource) {
    List<OnboardDatasetMetricDTO> dtos = onboardDatasetMetricDAO.findByDataSource(dataSource);
    return dtos;
  }


  @GET
  @Path("/view/dataSource/{dataSource}/onboarded/{onboarded}")
  @Produces(MediaType.APPLICATION_JSON)
  public List<OnboardDatasetMetricDTO> viewOnboardConfigsByDatasourceAndOnboarded(
      @PathParam("dataSource") String dataSource, @PathParam("onboarded") boolean onboarded) {
    List<OnboardDatasetMetricDTO> dtos = onboardDatasetMetricDAO.findByDataSourceAndOnboarded(dataSource, onboarded);
    return dtos;
  }


  /**
   * Create this by providing json payload as follows:
   *
   *    curl -H "Content-Type: application/json" -X POST -d <payload> <url>
   *    Eg: curl -H "Content-Type: application/json" -X POST -d
   *            '{"datasetName":"xyz","metricName":"xyz", "dataSource":"PinotThirdeyeDataSource", "properties": { "prop1":"1", "prop2":"2"}}'
   *                http://localhost:8080/onboard/create
   * @param payload
   */
  @POST
  @Path("/create")
  public Response createOnboardConfig(String payload) {
    OnboardDatasetMetricDTO onboardConfig = null;
    Response response = null;
    try {
      onboardConfig = OBJECT_MAPPER.readValue(payload, OnboardDatasetMetricDTO.class);
      Long id = onboardDatasetMetricDAO.save(onboardConfig);
      response = Response.status(Status.OK).entity(String.format("Created config with id %d", id)).build();
    } catch (Exception e) {
      response = Response.status(Status.INTERNAL_SERVER_ERROR)
          .entity(String.format("Invalid payload %s %s",  payload, e)).build();
    }
    return response;
  }


  @DELETE
  @Path("/delete/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response deleteOnboardConfig(@PathParam("id") Long id) {
    Response response = Response.status(Status.NOT_FOUND).build();
    OnboardDatasetMetricDTO dto = onboardDatasetMetricDAO.findById(id);
    if (dto != null) {
      onboardDatasetMetricDAO.delete(dto);
      response = Response.ok().build();
    }
    return response;
  }

  @POST
  @Path("update/{id}/{onboarded}")
  public void toggleOnboarded(@PathParam("id") Long id, @PathParam("onboarded") boolean onboarded) {
    OnboardDatasetMetricDTO onboardConfig = onboardDatasetMetricDAO.findById(id);
    onboardConfig.setOnboarded(onboarded);
    onboardDatasetMetricDAO.update(onboardConfig);
  }

}
