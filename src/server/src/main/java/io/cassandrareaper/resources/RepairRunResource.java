/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.cassandrareaper.resources;

import io.cassandrareaper.AppContext;
import io.cassandrareaper.ReaperException;
import io.cassandrareaper.core.Cluster;
import io.cassandrareaper.core.RepairRun;
import io.cassandrareaper.core.RepairRun.RunState;
import io.cassandrareaper.core.RepairSegment;
import io.cassandrareaper.core.RepairUnit;
import io.cassandrareaper.resources.view.RepairRunStatus;
import io.cassandrareaper.service.RepairRunService;
import io.cassandrareaper.service.RepairUnitService;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.Nullable;
import javax.validation.ValidationException;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.cassandra.repair.RepairParallelism;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkNotNull;

@Path("/repair_run")
@Produces(MediaType.APPLICATION_JSON)
public final class RepairRunResource {

  private static final Logger LOG = LoggerFactory.getLogger(RepairRunResource.class);

  private final AppContext context;
  private final RepairUnitService repairUnitService;
  private final RepairRunService repairRunService;

  public RepairRunResource(AppContext context) {
    this.context = context;
    this.repairUnitService = RepairUnitService.create(context);
    this.repairRunService = RepairRunService.create(context);
  }

  /**
   * Endpoint used to create a repair run. Does not allow triggering the run. triggerRepairRun()
   * must be called to initiate the repair. Creating a repair run includes generating the repair
   * segments.
   *
   * <p>Notice that query parameter "tables" can be a single String, or a comma-separated list of
   * table names. If the "tables" parameter is omitted, and only the keyspace is defined, then
   * created repair run will target all the tables in the keyspace.
   *
   * @return repair run ID in case of everything going well, and a status code 500 in case of
   *     errors.
   */
  @POST
  public Response addRepairRun(
      @Context UriInfo uriInfo,
      @QueryParam("clusterName") Optional<String> clusterName,
      @QueryParam("keyspace") Optional<String> keyspace,
      @QueryParam("tables") Optional<String> tableNamesParam,
      @QueryParam("owner") Optional<String> owner,
      @QueryParam("cause") Optional<String> cause,
      @QueryParam("segmentCount") Optional<Integer> segmentCountPerNode,
      @QueryParam("repairParallelism") Optional<String> repairParallelism,
      @QueryParam("intensity") Optional<String> intensityStr,
      @QueryParam("incrementalRepair") Optional<String> incrementalRepairStr,
      @QueryParam("nodes") Optional<String> nodesToRepairParam,
      @QueryParam("datacenters") Optional<String> datacentersToRepairParam,
      @QueryParam("blacklistedTables") Optional<String> blacklistedTableNamesParam) {

    try {
      final Response possibleFailedResponse = RepairRunResource.checkRequestForAddRepair(
          context,
          clusterName,
          keyspace,
          owner,
          segmentCountPerNode,
          repairParallelism,
          intensityStr,
          incrementalRepairStr,
          nodesToRepairParam,
          datacentersToRepairParam);
      if (null != possibleFailedResponse) {
        return possibleFailedResponse;
      }

      Double intensity;
      if (intensityStr.isPresent()) {
        intensity = Double.parseDouble(intensityStr.get());
      } else {
        intensity = context.config.getRepairIntensity();
        LOG.debug("no intensity given, so using default value: {}", intensity);
      }

      Boolean incrementalRepair;
      if (incrementalRepairStr.isPresent()) {
        incrementalRepair = Boolean.parseBoolean(incrementalRepairStr.get());
      } else {
        incrementalRepair = context.config.getIncrementalRepair();
        LOG.debug("no incremental repair given, so using default value: {}", incrementalRepair);
      }

      int segments = context.config.getSegmentCountPerNode();
      if (!incrementalRepair) {
        if (segmentCountPerNode.isPresent()) {
          LOG.debug(
              "using given segment count {} instead of configured value {}",
              segmentCountPerNode.get(),
              context.config.getSegmentCount());
          segments = segmentCountPerNode.get();
        }
      } else {
        // hijack the segment count in case of incremental repair
        segments = -1;
      }

      final Cluster cluster = context.storage.getCluster(Cluster.toSymbolicName(clusterName.get())).get();
      Set<String> tableNames;
      try {
        tableNames = repairRunService.getTableNamesBasedOnParam(cluster, keyspace.get(), tableNamesParam);
      } catch (IllegalArgumentException ex) {
        LOG.error(ex.getMessage(), ex);
        return Response.status(Response.Status.NOT_FOUND).entity(ex.getMessage()).build();
      }

      Set<String> blacklistedTableNames;
      try {
        blacklistedTableNames
            = repairRunService.getTableNamesBasedOnParam(cluster, keyspace.get(), blacklistedTableNamesParam);
      } catch (IllegalArgumentException ex) {
        LOG.error(ex.getMessage(), ex);
        return Response.status(Response.Status.NOT_FOUND).entity(ex.getMessage()).build();
      }

      final Set<String> nodesToRepair;
      try {
        nodesToRepair = repairRunService.getNodesToRepairBasedOnParam(cluster, nodesToRepairParam);
      } catch (IllegalArgumentException ex) {
        LOG.error(ex.getMessage(), ex);
        return Response.status(Response.Status.NOT_FOUND).entity(ex.getMessage()).build();
      }

      final Set<String> datacentersToRepair;
      try {
        datacentersToRepair = RepairRunService
            .getDatacentersToRepairBasedOnParam(cluster, datacentersToRepairParam);

      } catch (IllegalArgumentException ex) {
        LOG.error(ex.getMessage(), ex);
        return Response.status(Response.Status.NOT_FOUND).entity(ex.getMessage()).build();
      }

      final RepairUnit theRepairUnit =
          repairUnitService.getNewOrExistingRepairUnit(
              cluster,
              keyspace.get(),
              tableNames,
              incrementalRepair,
              nodesToRepair,
              datacentersToRepair,
              blacklistedTableNames);

      if (theRepairUnit.getIncrementalRepair().booleanValue() != incrementalRepair) {
        return Response.status(Response.Status.BAD_REQUEST)
            .entity(
                "A repair run already exist for the same cluster/keyspace/table"
                + " but with a different incremental repair value. Requested value: "
                + incrementalRepair
                + " | Existing value: "
                + theRepairUnit.getIncrementalRepair())
            .build();
      }

      RepairParallelism parallelism = context.config.getRepairParallelism();
      if (repairParallelism.isPresent()) {
        LOG.debug(
            "using given repair parallelism {} instead of configured value {}",
            repairParallelism.get(),
            context.config.getRepairParallelism());

        parallelism = RepairParallelism.valueOf(repairParallelism.get().toUpperCase());
      }

      if (incrementalRepair) {
        parallelism = RepairParallelism.PARALLEL;
      }

      final RepairRun newRepairRun =
          repairRunService.registerRepairRun(
              cluster,
              theRepairUnit,
              cause,
              owner.get(),
              0,
              segments,
              parallelism,
              intensity);

      return Response.created(buildRepairRunUri(uriInfo, newRepairRun))
          .entity(new RepairRunStatus(newRepairRun, theRepairUnit, 0))
          .build();

    } catch (ReaperException e) {
      LOG.error(e.getMessage(), e);
      return Response.status(500).entity(e.getMessage()).build();
    }
  }

  /**
   * @return Response instance in case there is a problem, or null if everything is ok.
   */
  @Nullable
  static Response checkRequestForAddRepair(
      AppContext context,
      Optional<String> clusterName,
      Optional<String> keyspace,
      Optional<String> owner,
      Optional<Integer> segmentCountPerNode,
      Optional<String> repairParallelism,
      Optional<String> intensityStr,
      Optional<String> incrementalRepairStr,
      Optional<String> nodesStr,
      Optional<String> datacentersStr) {

    if (!clusterName.isPresent()) {
      return createMissingArgumentResponse("clusterName");
    }
    if (!keyspace.isPresent()) {
      return createMissingArgumentResponse("keyspace");
    }
    if (!owner.isPresent()) {
      return createMissingArgumentResponse("owner");
    }
    if (segmentCountPerNode.isPresent()
        && (segmentCountPerNode.get() < 0 || segmentCountPerNode.get() > 1000)) {
      return Response.status(Response.Status.BAD_REQUEST)
          .entity("invalid query parameter \"segmentCountPerNode\", maximum value is 100000")
          .build();
    }
    if (repairParallelism.isPresent()) {
      try {
        checkRepairParallelismString(repairParallelism.get());
      } catch (ReaperException ex) {
        LOG.error(ex.getMessage(), ex);
        return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
      }
    }

    if (intensityStr.isPresent()) {
      try {
        // @todo all BAD_REQUEST responses should be instead thrown ValidationExceptions, so this method returns void
        parseIntensity(intensityStr.get());
      } catch (ValidationException ex) {
        return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
      }
    }

    if (incrementalRepairStr.isPresent()
        && (!incrementalRepairStr.get().toUpperCase().contentEquals("TRUE")
        && !incrementalRepairStr.get().toUpperCase().contentEquals("FALSE"))) {
      return Response.status(Response.Status.BAD_REQUEST)
          .entity("invalid query parameter \"incrementalRepair\", expecting [True,False]")
          .build();
    }
    final Optional<Cluster> cluster = context.storage.getCluster(Cluster.toSymbolicName(clusterName.get()));
    if (!cluster.isPresent()) {
      return Response.status(Response.Status.NOT_FOUND)
          .entity("No cluster found with name \"" + clusterName.get() + "\", did you register your cluster first?")
          .build();
    }

    if (!datacentersStr.or("").isEmpty() && !nodesStr.or("").isEmpty()) {
      return Response.status(Response.Status.BAD_REQUEST)
          .entity(
              "Parameters \"datacenters\" and \"nodes\" are mutually exclusive. Please fill just one between the two.")
          .build();
    }

    return null;
  }

  /**
   * Modifies a state of the repair run.
   *
   * <p>Currently supports NOT_STARTED|PAUSED to RUNNING and RUNNING to PAUSED.
   *
   * @return OK if all goes well NOT_MODIFIED if new state is the same as the old one, and 409
   *     (CONFLICT) if transition is not supported.
   */
  @PUT
  @Path("/{id}/state/{state}")
  public Response modifyRunState(
      @Context UriInfo uriInfo,
      @PathParam("id") UUID repairRunId,
      @PathParam("state") Optional<String> stateStr)
      throws ReaperException {

    LOG.info("modify repair run state called with: id = {}, state = {}", repairRunId, stateStr);
    try {
      if (!stateStr.isPresent()) {
        return createMissingArgumentResponse("state");
      }

      Optional<RepairRun> repairRun = context.storage.getRepairRun(repairRunId);
      if (!repairRun.isPresent()) {
        return Response.status(Status.NOT_FOUND).entity("repair run " + repairRunId + " doesn't exist").build();
      }
      final RepairRun.RunState newState = parseRunState(stateStr.get());

      Optional<RepairUnit> repairUnit = context.storage.getRepairUnit(repairRun.get().getRepairUnitId());
      if (!repairUnit.isPresent()) {
        String errMsg = "repair unit with id " + repairRun.get().getRepairUnitId() + " not found";
        LOG.error(errMsg);
        return Response.status(Status.CONFLICT).entity(errMsg).build();
      }

      if (isUnitAlreadyRepairing(repairRun.get())) {
        String errMsg = "repair unit already has run " + repairRun.get().getRepairUnitId() + " in RUNNING state";
        LOG.error(errMsg);
        return Response.status(Status.CONFLICT).entity(errMsg).build();
      }

      final RunState oldState = repairRun.get().getRunState();
      if (oldState == newState) {
        String msg = "given \"state\" " + stateStr + " is same as the current run state";
        return Response.status(Status.NOT_MODIFIED).entity(msg).build();
      }

      int segmentsRepaired = getSegmentAmountForRepairRun(repairRunId);
      if (isStarting(oldState, newState)) {
        return startRun(repairRun.get(), repairUnit.get(), segmentsRepaired);
      } else if (isPausing(oldState, newState)) {
        return pauseRun(repairRun.get(), repairUnit.get(), segmentsRepaired);
      } else if (isResuming(oldState, newState) || isRetrying(oldState, newState)) {
        return resumeRun(repairRun.get(), repairUnit.get(), segmentsRepaired);
      } else if (isAborting(oldState, newState)) {
        return abortRun(repairRun.get(), repairUnit.get(), segmentsRepaired);
      } else {
        String errMsg = String.format("Transition %s->%s not supported.", oldState.toString(), newState.toString());
        LOG.error(errMsg);
        return Response.status(Status.METHOD_NOT_ALLOWED).entity(errMsg).build();
      }
    } catch (ValidationException ex) {
      return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
    }
  }

  /**
   * Modifies the intensity of the repair run.
   *
   * @return OK if all goes well NOT_MODIFIED if new state is the same as the old one, and 409
   *     (CONFLICT) if transition is not supported.
   */
  @PUT
  @Path("/{id}/intensity/{intensity}")
  public Response modifyRunIntensity(
      @Context UriInfo uriInfo,
      @PathParam("id") UUID repairRunId,
      @PathParam("intensity") Optional<String> intensityStr)
      throws ReaperException {

    LOG.info("modify repair run intensity called with: id = {}, state = {}", repairRunId, intensityStr);
    try {
      if (!intensityStr.isPresent()) {
        return createMissingArgumentResponse("intensity");
      }
      final double intensity = parseIntensity(intensityStr.get());

      Optional<RepairRun> repairRun = context.storage.getRepairRun(repairRunId);
      if (!repairRun.isPresent()) {
        return Response.status(Status.NOT_FOUND).entity("repair run " + repairRunId + " doesn't exist").build();
      }

      Optional<RepairUnit> repairUnit = context.storage.getRepairUnit(repairRun.get().getRepairUnitId());
      if (!repairUnit.isPresent()) {
        String errMsg = "repair unit with id " + repairRun.get().getRepairUnitId() + " not found";
        LOG.error(errMsg);
        return Response.status(Response.Status.NOT_FOUND).entity(errMsg).build();
      }

      if (RunState.PAUSED != repairRun.get().getRunState() && RunState.NOT_STARTED != repairRun.get().getRunState()) {
        return Response.status(Status.CONFLICT).entity("repair run must first be paused").build();
      }

      int segmentsRepaired = getSegmentAmountForRepairRun(repairRunId);
      return updateRunIntensity(repairRun.get(), repairUnit.get(), segmentsRepaired, intensity);
    } catch (ValidationException ex) {
      return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
    }
  }

  /**
   * MOVED_PERMANENTLY to PUT repair_run/{id}/state/{state}
   */
  @PUT
  @Path("/{id}")
  @Deprecated
  public Response oldModifyRunState(
      @Context UriInfo uriInfo,
      @PathParam("id") UUID repairRunId,
      @QueryParam("state") Optional<String> stateStr)
      throws ReaperException {

    try {
      if (!stateStr.isPresent()) {
        return createMissingArgumentResponse("state");
      }
      RepairRun.RunState state = parseRunState(stateStr.get());

      Optional<RepairRun> repairRun = context.storage.getRepairRun(repairRunId);
      if (!repairRun.isPresent()) {
        return Response.status(Status.NOT_FOUND).entity("repair run " + repairRunId + " doesn't exist").build();
      }

      URI redirectUri = uriInfo
          .getRequestUriBuilder()
          .replacePath(String.format("repair_run/%s/state/%s", repairRun.get().getId().toString(), state))
          .replaceQuery("")
          .build();

      return Response.seeOther(redirectUri).build();
    } catch (ValidationException ex) {
      return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
    }
  }

  private static boolean isStarting(RepairRun.RunState oldState, RepairRun.RunState newState) {
    return oldState == RepairRun.RunState.NOT_STARTED && newState == RepairRun.RunState.RUNNING;
  }

  private static boolean isPausing(RepairRun.RunState oldState, RepairRun.RunState newState) {
    return oldState == RepairRun.RunState.RUNNING && newState == RepairRun.RunState.PAUSED;
  }

  private static boolean isResuming(RepairRun.RunState oldState, RepairRun.RunState newState) {
    return oldState == RepairRun.RunState.PAUSED && newState == RepairRun.RunState.RUNNING;
  }

  private static boolean isRetrying(RepairRun.RunState oldState, RepairRun.RunState newState) {
    return oldState == RepairRun.RunState.ERROR && newState == RepairRun.RunState.RUNNING;
  }

  private static boolean isAborting(RepairRun.RunState oldState, RepairRun.RunState newState) {
    return oldState != RepairRun.RunState.ERROR && newState == RepairRun.RunState.ABORTED;
  }

  private boolean isUnitAlreadyRepairing(RepairRun repairRun) {
    return context.storage.getRepairRunsForUnit(repairRun.getRepairUnitId()).stream()
        .anyMatch((run) -> (!run.getId().equals(repairRun.getId()) && run.getRunState().equals(RunState.RUNNING)));
  }

  private int getSegmentAmountForRepairRun(UUID repairRunId) {
    return context.storage.getSegmentAmountForRepairRunWithState(repairRunId, RepairSegment.State.DONE);
  }

  private Response startRun(RepairRun repairRun, RepairUnit repairUnit, int segmentsRepaired) throws ReaperException {
    LOG.info("Starting run {}", repairRun.getId());
    final RepairRun newRun = context.repairManager.startRepairRun(repairRun);
    return Response.status(Response.Status.OK)
        .entity(new RepairRunStatus(newRun, repairUnit, segmentsRepaired))
        .build();
  }

  private Response pauseRun(RepairRun repairRun, RepairUnit repairUnit, int segmentsRepaired) throws ReaperException {
    LOG.info("Pausing run {}", repairRun.getId());
    final RepairRun newRun = context.repairManager.pauseRepairRun(repairRun);
    return Response.ok().entity(new RepairRunStatus(newRun, repairUnit, segmentsRepaired)).build();
  }

  private Response resumeRun(RepairRun repairRun, RepairUnit repairUnit, int segmentsRepaired) throws ReaperException {
    LOG.info("Resuming run {}", repairRun.getId());
    final RepairRun newRun = context.repairManager.startRepairRun(repairRun);
    return Response.ok().entity(new RepairRunStatus(newRun, repairUnit, segmentsRepaired)).build();
  }

  private Response abortRun(RepairRun repairRun, RepairUnit repairUnit, int segmentsRepaired) throws ReaperException {
    LOG.info("Aborting run {}", repairRun.getId());
    final RepairRun newRun = context.repairManager.abortRepairRun(repairRun);
    return Response.ok().entity(new RepairRunStatus(newRun, repairUnit, segmentsRepaired)).build();
  }

  private Response updateRunIntensity(RepairRun run, RepairUnit unit, int repaired, double intensity)
      throws ReaperException {

    LOG.info("Editing run {}", run.getId());
    RepairRun newRun = context.repairManager.updateRepairRunIntensity(run, intensity);
    return Response.ok().entity(new RepairRunStatus(newRun, unit, repaired)).build();
  }

  /**
   * @return detailed information about a repair run.
   */
  @GET
  @Path("/{id}")
  public Response getRepairRun(
      @PathParam("id") UUID repairRunId) {

    LOG.debug("get repair_run called with: id = {}", repairRunId);
    final Optional<RepairRun> repairRun = context.storage.getRepairRun(repairRunId);
    if (repairRun.isPresent()) {
      RepairRunStatus repairRunStatus = getRepairRunStatus(repairRun.get());
      return Response.ok().entity(repairRunStatus).build();
    } else {
      return Response.status(404).entity("repair run with id " + repairRunId + " doesn't exist").build();
    }
  }

  /**
   * @return list the segments of a repair run.
   */
  @GET
  @Path("/{id}/segments")
  public Response getRepairRunSegments(@PathParam("id") UUID repairRunId) {

    LOG.debug("get repair_run called with: id = {}", repairRunId);
    final Optional<RepairRun> repairRun = context.storage.getRepairRun(repairRunId);
    if (repairRun.isPresent()) {
      Collection<RepairSegment> segments = context.storage.getRepairSegmentsForRun(repairRunId);
      return Response.ok().entity(segments).build();
    } else {
      return Response.status(404)
          .entity("repair run with id " + repairRunId + " doesn't exist")
          .build();
    }
  }

  /**
   * @return Aborts a running segment.
   */
  @GET
  @Path("/{id}/segments/abort/{segment_id}")
  public Response getRepairRunSegments(
      @PathParam("id") UUID repairRunId, @PathParam("segment_id") UUID segmentId) {

    LOG.debug("abort segment called with: run id = {} and segment id = {}", repairRunId, segmentId);
    final Optional<RepairRun> repairRun = context.storage.getRepairRun(repairRunId);
    if (repairRun.isPresent()) {
      if (RepairRun.RunState.RUNNING == repairRun.get().getRunState()
          || RepairRun.RunState.PAUSED == repairRun.get().getRunState()) {
        RepairSegment segment = context.repairManager.abortSegment(repairRunId, segmentId);
        return Response.ok().entity(segment).build();
      } else {
        return Response.status(Response.Status.CONFLICT)
            .entity(
                "Cannot abort segment on repair run with status " + repairRun.get().getRunState())
            .build();
      }
    } else {
      return Response.status(404)
          .entity("repair run with id " + repairRunId + " doesn't exist")
          .build();
    }
  }

  /**
   * @return all know repair runs for a cluster.
   */
  @GET
  @Path("/cluster/{cluster_name}")
  public Response getRepairRunsForCluster(
      @PathParam("cluster_name") String clusterName) {

    LOG.debug("get repair run for cluster called with: cluster_name = {}", clusterName);
    final Collection<RepairRun> repairRuns = context.storage.getRepairRunsForCluster(clusterName);
    final Collection<RepairRunStatus> repairRunViews = new ArrayList<>();
    for (final RepairRun repairRun : repairRuns) {
      repairRunViews.add(getRepairRunStatus(repairRun));
    }
    return Response.ok().entity(repairRunViews).build();
  }

  /**
   * @return only a status of a repair run, not the entire repair run info.
   */
  private RepairRunStatus getRepairRunStatus(RepairRun repairRun) {
    final Optional<RepairUnit> repairUnit = context.storage.getRepairUnit(repairRun.getRepairUnitId());
    Preconditions.checkState(repairUnit.isPresent(), "no repair unit found with id: %s", repairRun.getRepairUnitId());
    final int segmentsRepaired = getSegmentAmountForRepairRun(repairRun.getId());
    return new RepairRunStatus(repairRun, repairUnit.get(), segmentsRepaired);
  }

  /**
   * Crafts an URI used to identify given repair run.
   *
   * @return The created resource URI.
   */
  private URI buildRepairRunUri(UriInfo uriInfo, RepairRun repairRun) {
    final String newRepairRunPathPart = "repair_run/" + repairRun.getId();
    URI runUri = null;
    try {
      runUri = new URL(uriInfo.getBaseUri().toURL(), newRepairRunPathPart).toURI();
    } catch (MalformedURLException | URISyntaxException e) {
      LOG.error(e.getMessage(), e);
    }
    checkNotNull(runUri, "failed to build repair run uri");
    return runUri;
  }

  /**
   * @param state comma-separated list of states to return. These states must match names of {@link
   *     io.cassandrareaper.core.RepairRun.RunState}.
   * @param cluster only return repair runs belonging to this cluster
   * @param keyspace only return repair runs belonging to this keyspace
   * @return All repair runs in the system if the param is absent, repair runs with state included in the state
   *       parameter otherwise.
   *        If the state parameter contains non-existing run states, BAD_REQUEST response is returned.
   */
  @GET
  public Response listRepairRuns(
      @QueryParam("state") Optional<String> state,
      @QueryParam("cluster_name") Optional<String> cluster,
      @QueryParam("keyspace_name") Optional<String> keyspace) {

    try {
      final List<RepairRunStatus> runStatuses = Lists.newArrayList();
      final Set desiredStates = splitStateParam(state);
      if (desiredStates == null) {
        return Response.status(Response.Status.BAD_REQUEST).build();
      }

      Collection<Cluster> clusters = cluster
          .transform((clstr) -> context.storage.getCluster(clstr).get())
          .transform((clstr) -> (Collection<Cluster>)Collections.singleton(clstr))
          .or(context.storage.getClusters());

      for (final Cluster clstr : clusters) {
        Collection<RepairRun> runs = context.storage.getRepairRunsForCluster(clstr.getName());
        runStatuses.addAll(
            (List<RepairRunStatus>) getRunStatuses(runs, desiredStates)
                .stream()
                .filter((run) -> !keyspace.isPresent()
                    || ((RepairRunStatus)run).getKeyspaceName().equals(keyspace.get()))
                .collect(Collectors.toList()));
      }

      return Response.status(Response.Status.OK).entity(runStatuses).build();
    } catch (ReaperException e) {
      LOG.error("Failed listing cluster statuses", e);
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
  }

  private List<RepairRunStatus> getRunStatuses(
      Collection<RepairRun> runs,
      Set<String> desiredStates) throws ReaperException {

    final List<RepairRunStatus> runStatuses = Lists.newArrayList();
    for (final RepairRun run : runs) {
      if (!desiredStates.isEmpty() && !desiredStates.contains(run.getRunState().name())) {
        continue;
      }
      final Optional<RepairUnit> runsUnit = context.storage.getRepairUnit(run.getRepairUnitId());
      if (runsUnit.isPresent()) {
        int segmentsRepaired = run.getSegmentCount();
        if (!run.getRunState().equals(RepairRun.RunState.DONE)) {
          segmentsRepaired = getSegmentAmountForRepairRun(run.getId());
        }

        runStatuses.add(new RepairRunStatus(run, runsUnit.get(), segmentsRepaired));
      } else {
        final String errMsg = String.format("Found repair run %s with no associated repair unit", run.getId());
        LOG.error(errMsg);
        throw new ReaperException("Internal server error : " + errMsg);
      }
    }

    return runStatuses;
  }

  static Set splitStateParam(Optional<String> state) {
    if (state.isPresent()) {
      final Iterable<String> chunks = RepairRunService.COMMA_SEPARATED_LIST_SPLITTER.split(state.get());
      for (final String chunk : chunks) {
        try {
          RepairRun.RunState.valueOf(chunk.toUpperCase());
        } catch (IllegalArgumentException e) {
          LOG.warn("Listing repair runs called with erroneous states: {}", state.get(), e);
          return null;
        }
      }
      return Sets.newHashSet(chunks);
    } else {
      return Sets.newHashSet();
    }
  }

  /**
   * Delete a RepairRun object with given id.
   *
   * <p>
   * Repair run can be only deleted when it is not running. When Repair run is deleted, all the related RepairSegmen
   * instances will be deleted also.
   *
   * @param runId The id for the RepairRun instance to delete.
   * @param owner The assigned owner of the deleted resource. Must match the stored one.
   * @return The deleted RepairRun instance, with state overwritten to string "DELETED".
   */
  @DELETE
  @Path("/{id}")
  public Response deleteRepairRun(
      @PathParam("id") UUID runId,
      @QueryParam("owner") Optional<String> owner) {

    LOG.info("delete repair run called with runId: {}, and owner: {}", runId, owner);
    if (!owner.isPresent()) {
      return Response.status(Response.Status.BAD_REQUEST)
          .entity("required query parameter \"owner\" is missing")
          .build();
    }
    final Optional<RepairRun> runToDelete = context.storage.getRepairRun(runId);
    if (runToDelete.isPresent()) {
      if (runToDelete.get().getRunState() == RepairRun.RunState.RUNNING) {
        return Response.status(Response.Status.FORBIDDEN)
            .entity("Repair run with id \"" + runId + "\" is currently running, and must be stopped before deleting")
            .build();
      }
      if (!runToDelete.get().getOwner().equalsIgnoreCase(owner.get())) {
        return Response.status(Response.Status.FORBIDDEN)
            .entity("Repair run with id \"" + runId + "\" is not owned by the user you defined: " + owner.get())
            .build();
      }
      if (context.storage.getSegmentAmountForRepairRunWithState(runId, RepairSegment.State.RUNNING) > 0) {
        return Response.status(Response.Status.FORBIDDEN)
            .entity(
                "Repair run with id \""
                + runId
                + "\" has a running segment, which must be waited to finish before deleting")
            .build();
      }
      // Need to get the RepairUnit before it's possibly deleted.
      final Optional<RepairUnit> unitPossiblyDeleted
          = context.storage.getRepairUnit(runToDelete.get().getRepairUnitId());

      final int segmentsRepaired = getSegmentAmountForRepairRun(runId);
      final Optional<RepairRun> deletedRun = context.storage.deleteRepairRun(runId);
      if (deletedRun.isPresent()) {
        final RepairRunStatus repairRunStatus
            = new RepairRunStatus(deletedRun.get(), unitPossiblyDeleted.get(), segmentsRepaired);

        return Response.ok().entity(repairRunStatus).build();
      }
    }
    try {
      // safety clean, in case of zombie segments
      context.storage.deleteRepairRun(runId);
    } catch (RuntimeException ignore) { }
    return Response.status(Response.Status.NOT_FOUND).entity("Repair run with id \"" + runId + "\" not found").build();
  }

  private static void checkRepairParallelismString(String repairParallelism) throws ReaperException {
    try {
      RepairParallelism.valueOf(repairParallelism.toUpperCase());
    } catch (IllegalArgumentException ex) {
      throw new ReaperException(
          "invalid repair parallelism given \""
          + repairParallelism
          + "\", must be one of: "
          + Arrays.toString(RepairParallelism.values()),
          ex);
    }
  }

  private static Response createMissingArgumentResponse(String argumentName) {
    return Response.status(Status.BAD_REQUEST).entity(argumentName + " argument missing").build();
  }

  private static RunState parseRunState(String input) throws ValidationException {
    try {
      return RunState.valueOf(input.toUpperCase());
    } catch (IllegalArgumentException ex) {
      throw new ValidationException("invalid \"state\" argument: " + input, ex);
    }
  }

  private static double parseIntensity(String input) throws ValidationException {
    try {
      double intensity = Double.parseDouble(input);
      if (intensity <= 0.0 || intensity > 1.0) {
        throw new ValidationException("query parameter \"intensity\" must be in range (0.0, 1.0]: " + input);
      }
      return intensity;
    } catch (NumberFormatException ex) {
      throw new ValidationException("invalid value for query parameter \"intensity\": " + input, ex);
    }
  }
}
