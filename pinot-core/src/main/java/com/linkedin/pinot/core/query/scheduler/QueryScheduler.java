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

package com.linkedin.pinot.core.query.scheduler;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.linkedin.pinot.common.exception.QueryException;
import com.linkedin.pinot.common.metrics.ServerGauge;
import com.linkedin.pinot.common.metrics.ServerMeter;
import com.linkedin.pinot.common.metrics.ServerMetrics;
import com.linkedin.pinot.common.metrics.ServerQueryPhase;
import com.linkedin.pinot.common.query.QueryExecutor;
import com.linkedin.pinot.common.query.ServerQueryRequest;
import com.linkedin.pinot.common.query.context.TimerContext;
import com.linkedin.pinot.common.request.InstanceRequest;
import com.linkedin.pinot.common.response.ProcessingException;
import com.linkedin.pinot.common.utils.DataTable;
import com.linkedin.pinot.core.common.datatable.DataTableImplV2;
import com.linkedin.pinot.core.query.scheduler.resources.QueryExecutorService;
import com.linkedin.pinot.core.query.scheduler.resources.ResourceManager;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Abstract class providing common scheduler functionality
 * including query runner and query worker pool
 */
public abstract class QueryScheduler {

  private static final Logger LOGGER = LoggerFactory.getLogger(QueryScheduler.class);

  protected final ServerMetrics serverMetrics;
  protected final QueryExecutor queryExecutor;
  protected final ResourceManager resourceManager;
  protected volatile boolean isRunning = false;

  /**
   * Constructor to initialize QueryScheduler
   * @param queryExecutor QueryExecutor engine to use
   * @param resourceManager for managing server thread resources
   * @param serverMetrics server metrics collector
   */
  public QueryScheduler(@Nonnull QueryExecutor queryExecutor, @Nonnull ResourceManager resourceManager,
      @Nonnull ServerMetrics serverMetrics) {
    Preconditions.checkNotNull(queryExecutor);
    Preconditions.checkNotNull(resourceManager);
    Preconditions.checkNotNull(serverMetrics);

    this.serverMetrics = serverMetrics;
    this.resourceManager = resourceManager;
    this.queryExecutor = queryExecutor;
  }

  /**
   * Submit a query for execution. The query will be scheduled for execution as per the scheduling algorithm
   * @param queryRequest query to schedule for execution
   * @return Listenable future for query result representing serialized response. It is possible that the
   *    future may return immediately or be scheduled for execution at a later time.
   */
  public abstract @Nonnull ListenableFuture<byte[]> submit(@Nullable ServerQueryRequest queryRequest);

  /**
   * Query scheduler name for logging
   */
  public abstract String name();

  /**
   * Start query scheduler thread
   */
  public void start() {
    isRunning = true;
  }

  /**
   * stop the scheduler and shutdown services
   */
  public void stop() {
    // don't stop resourcemanager yet...we need to wait for all running queries to finish
    isRunning = false;
  }


  @VisibleForTesting
  public ExecutorService getQueryWorkers() {
    return resourceManager.getQueryWorkers();
  }

  /**
   * Create a future task for the query
   * @param request incoming query request
   * @param e executor service to use for parallelizing query. This is passed to the QueryExecutor
   * @return Future task that can be scheduled for execution on an ExecutorService. Ideally, this future
   * should be executed on a different executor service than {@code e} to avoid deadlock.
   */
  protected ListenableFutureTask<byte[]> createQueryFutureTask(@Nonnull final ServerQueryRequest request,
      @Nonnull final QueryExecutorService e) {
    return ListenableFutureTask.create(new Callable<byte[]>() {
      @Override
      public byte[] call()
          throws Exception {
        return processQueryAndSerialize(request, e);
      }
    });
  }

  /**
   * Process query and serialize response
   * @param request incoming query request
   * @param executorService Executor service to use for parallelizing query processing
   * @return serialized query response
   */
  @Nullable
  protected byte[] processQueryAndSerialize(@Nonnull final ServerQueryRequest request,
      @Nonnull final ExecutorService executorService) {
    DataTable dataTable;
    try {
      dataTable = queryExecutor.processQuery(request, executorService);
    } catch (Exception e) {
      // For not handled exceptions
      serverMetrics.addMeteredGlobalValue(ServerMeter.UNCAUGHT_EXCEPTIONS, 1);
      dataTable = new DataTableImplV2();
      dataTable.addException(QueryException.getException(QueryException.INTERNAL_ERROR, e));
    }
    InstanceRequest instanceRequest = request.getInstanceRequest();
    long requestId = instanceRequest.getRequestId();
    Map<String, String> dataTableMetadata = dataTable.getMetadata();
    dataTableMetadata.put(DataTable.REQUEST_ID_METADATA_KEY, Long.toString(requestId));

    byte[] responseData = serializeDataTable(request, dataTable);

    // Log the statistics
    TimerContext timerContext = request.getTimerContext();
    LOGGER.info(
        "Processed requestId={},table={},reqSegments={},prunedToSegmentCount={},totalExecMs={},totalTimeMs={},broker={},numDocsScanned={},scanInFilter={},scanPostFilter={},sched={}",
        requestId,
        request.getTableName(),
        instanceRequest.getSearchSegments().size(),
        request.getSegmentCountAfterPruning(),
        timerContext.getPhaseDurationMs(ServerQueryPhase.QUERY_PROCESSING),
        timerContext.getPhaseDurationMs(ServerQueryPhase.TOTAL_QUERY_TIME),
        instanceRequest.getBrokerId(),
        getMetadataValue(dataTableMetadata, DataTable.NUM_DOCS_SCANNED_METADATA_KEY),
        getMetadataValue(dataTableMetadata, DataTable.NUM_ENTRIES_SCANNED_IN_FILTER_METADATA_KEY),
        getMetadataValue(dataTableMetadata, DataTable.NUM_ENTRIES_SCANNED_POST_FILTER_METADATA_KEY),
        name());
    serverMetrics.setValueOfTableGauge(request.getTableName(), ServerGauge.NUM_SEGMENTS_SEARCHED, request.getSegmentCountAfterPruning());

    return responseData;
  }

  protected String getMetadataValue(Map<String, String> metadata, String key) {
    String val = metadata.get(key);
    return (val == null) ? "" : val;
  }

  /**
   * Serialize the DataTable response for query request
   * @param queryRequest Server query request for which response is serialized
   * @param instanceResponse DataTable to serialize
   * @return serialized response bytes
   */
  @Nullable
  public static byte[] serializeDataTable(@Nonnull ServerQueryRequest queryRequest,
      @Nonnull DataTable instanceResponse) {
    TimerContext timerContext = queryRequest.getTimerContext();
    TimerContext.Timer responseSerializationTimer =
        timerContext.startNewPhaseTimer(ServerQueryPhase.RESPONSE_SERIALIZATION);

    byte[] responseByte;
    InstanceRequest instanceRequest = queryRequest.getInstanceRequest();
    try {
      responseByte = instanceResponse.toBytes();
    } catch (Exception e) {
      queryRequest.getServerMetrics().addMeteredGlobalValue(ServerMeter.RESPONSE_SERIALIZATION_EXCEPTIONS, 1);
      LOGGER.error("Caught exception while serializing response for requestId: {}, brokerId: {}",
          instanceRequest.getRequestId(), instanceRequest.getBrokerId(), e);
      responseByte = null;
    }

    responseSerializationTimer.stopAndRecord();
    timerContext.startNewPhaseTimerAtNs(ServerQueryPhase.TOTAL_QUERY_TIME, timerContext.getQueryArrivalTimeNs())
        .stopAndRecord();

    return responseByte;
  }

  /**
   * Error response future in case of internal error where query response is not available. This can happen
   * if the query can not be executed or
   * @param queryRequest
   * @param error error code to send
   * @return
   */
  protected ListenableFuture<byte[]> immediateErrorResponse(ServerQueryRequest queryRequest, ProcessingException error) {
    DataTable result = new DataTableImplV2();
    result.addException(error);
    return Futures.immediateFuture(QueryScheduler.serializeDataTable(queryRequest, result));
  }
}
