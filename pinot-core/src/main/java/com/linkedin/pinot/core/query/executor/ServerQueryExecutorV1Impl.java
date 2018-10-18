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
package com.linkedin.pinot.core.query.executor;

import com.google.common.base.Preconditions;
import com.linkedin.pinot.common.data.DataManager;
import com.linkedin.pinot.common.exception.QueryException;
import com.linkedin.pinot.common.metrics.ServerMeter;
import com.linkedin.pinot.common.metrics.ServerMetrics;
import com.linkedin.pinot.common.metrics.ServerQueryPhase;
import com.linkedin.pinot.common.query.QueryExecutor;
import com.linkedin.pinot.common.query.ServerQueryRequest;
import com.linkedin.pinot.common.query.context.TimerContext;
import com.linkedin.pinot.common.request.BrokerRequest;
import com.linkedin.pinot.common.request.InstanceRequest;
import com.linkedin.pinot.common.utils.CommonConstants;
import com.linkedin.pinot.common.utils.DataTable;
import com.linkedin.pinot.core.common.datatable.DataTableBuilder;
import com.linkedin.pinot.core.common.datatable.DataTableImplV2;
import com.linkedin.pinot.core.data.manager.offline.InstanceDataManager;
import com.linkedin.pinot.core.data.manager.offline.SegmentDataManager;
import com.linkedin.pinot.core.data.manager.offline.TableDataManager;
import com.linkedin.pinot.core.indexsegment.IndexSegment;
import com.linkedin.pinot.core.plan.Plan;
import com.linkedin.pinot.core.plan.maker.InstancePlanMakerImplV2;
import com.linkedin.pinot.core.plan.maker.PlanMaker;
import com.linkedin.pinot.core.query.config.QueryExecutorConfig;
import com.linkedin.pinot.core.query.exception.BadQueryRequestException;
import com.linkedin.pinot.core.query.pruner.SegmentPrunerService;
import com.linkedin.pinot.core.query.pruner.SegmentPrunerServiceImpl;
import com.linkedin.pinot.core.util.trace.TraceContext;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ServerQueryExecutorV1Impl implements QueryExecutor {
  private static final Logger LOGGER = LoggerFactory.getLogger(ServerQueryExecutorV1Impl.class);
  private static final boolean PRINT_QUERY_PLAN = false;

  private InstanceDataManager _instanceDataManager = null;
  private SegmentPrunerService _segmentPrunerService = null;
  private PlanMaker _planMaker = null;
  private volatile boolean _isStarted = false;
  private long _defaultTimeOutMs = CommonConstants.Server.DEFAULT_QUERY_EXECUTOR_TIMEOUT_MS;
  private final Map<String, Long> _resourceTimeOutMsMap = new ConcurrentHashMap<>();
  private ServerMetrics _serverMetrics;

  public ServerQueryExecutorV1Impl() {
  }

  @Override
  public void init(Configuration configuration, DataManager dataManager, ServerMetrics serverMetrics)
      throws ConfigurationException {
    _serverMetrics = serverMetrics;
    _instanceDataManager = (InstanceDataManager) dataManager;
    QueryExecutorConfig queryExecutorConfig = new QueryExecutorConfig(configuration);
    if (queryExecutorConfig.getTimeOut() > 0) {
      _defaultTimeOutMs = queryExecutorConfig.getTimeOut();
    }
    LOGGER.info("Default timeout for query executor : {}", _defaultTimeOutMs);
    LOGGER.info("Trying to build SegmentPrunerService");
    _segmentPrunerService = new SegmentPrunerServiceImpl(queryExecutorConfig.getPrunerConfig());
    LOGGER.info("Trying to build QueryPlanMaker");
    _planMaker = new InstancePlanMakerImplV2(queryExecutorConfig);
    LOGGER.info("Trying to build QueryExecutorTimer");
  }

  @Override
  public DataTable processQuery(ServerQueryRequest queryRequest, ExecutorService executorService) {
    TimerContext timerContext = queryRequest.getTimerContext();
    TimerContext.Timer schedulerWaitTimer = timerContext.getPhaseTimer(ServerQueryPhase.SCHEDULER_WAIT);
    if (schedulerWaitTimer != null) {
      schedulerWaitTimer.stopAndRecord();
    }
    TimerContext.Timer queryProcessingTimer = timerContext.startNewPhaseTimer(ServerQueryPhase.QUERY_PROCESSING);

    InstanceRequest instanceRequest = queryRequest.getInstanceRequest();
    long requestId = instanceRequest.getRequestId();
    BrokerRequest brokerRequest = instanceRequest.getQuery();
    LOGGER.debug("Incoming request Id: {}, query: {}", requestId, brokerRequest);
    String tableName = brokerRequest.getQuerySource().getTableName();
    TableDataManager tableDataManager = _instanceDataManager.getTableDataManager(tableName);
    Preconditions.checkState(tableDataManager != null, "Failed to find data manager for table: {}", tableName);
    List<SegmentDataManager> queryableSegmentDataManagerList =
        acquireQueryableSegments(tableDataManager, instanceRequest);
    boolean enableTrace = instanceRequest.isEnableTrace();
    if (enableTrace) {
      TraceContext.register(requestId);
    }

    DataTable dataTable = null;
    try {
      TimerContext.Timer segmentPruneTimer = timerContext.startNewPhaseTimer(ServerQueryPhase.SEGMENT_PRUNING);
      long totalRawDocs = pruneSegments(tableDataManager, queryableSegmentDataManagerList, queryRequest);
      segmentPruneTimer.stopAndRecord();

      int numSegmentsMatched = queryableSegmentDataManagerList.size();
      queryRequest.setSegmentCountAfterPruning(numSegmentsMatched);
      LOGGER.debug("Matched {} segments", numSegmentsMatched);
      if (numSegmentsMatched == 0) {
        dataTable = DataTableBuilder.buildEmptyDataTable(brokerRequest);
        Map<String, String> metadata = dataTable.getMetadata();
        metadata.put(DataTable.TOTAL_DOCS_METADATA_KEY, String.valueOf(totalRawDocs));
        metadata.put(DataTable.NUM_DOCS_SCANNED_METADATA_KEY, "0");
        metadata.put(DataTable.NUM_ENTRIES_SCANNED_IN_FILTER_METADATA_KEY, "0");
        metadata.put(DataTable.NUM_ENTRIES_SCANNED_POST_FILTER_METADATA_KEY, "0");
      } else {
        TimerContext.Timer planBuildTimer = timerContext.startNewPhaseTimer(ServerQueryPhase.BUILD_QUERY_PLAN);
        Plan globalQueryPlan =
            _planMaker.makeInterSegmentPlan(queryableSegmentDataManagerList, brokerRequest, executorService,
                getResourceTimeOut(brokerRequest));
        planBuildTimer.stopAndRecord();

        if (PRINT_QUERY_PLAN) {
          LOGGER.debug("***************************** Query Plan for Request {} ***********************************",
              instanceRequest.getRequestId());
          globalQueryPlan.print();
          LOGGER.debug("*********************************** End Query Plan ***********************************");
        }

        TimerContext.Timer planExecTimer = timerContext.startNewPhaseTimer(ServerQueryPhase.QUERY_PLAN_EXECUTION);
        dataTable = globalQueryPlan.execute();
        planExecTimer.stopAndRecord();

        // Update the total docs in the metadata based on un-pruned segments.
        dataTable.getMetadata().put(DataTable.TOTAL_DOCS_METADATA_KEY, Long.toString(totalRawDocs));
      }
    } catch (Exception e) {
      _serverMetrics.addMeteredQueryValue(brokerRequest, ServerMeter.QUERY_EXECUTION_EXCEPTIONS, 1);

      // Do not log error for BadQueryRequestException because it's caused by bad query
      if (e instanceof BadQueryRequestException) {
        LOGGER.info("Caught BadQueryRequestException while processing requestId: {}, {}", requestId, e.getMessage());
      } else {
        LOGGER.error("Exception processing requestId {}", requestId, e);
      }

      dataTable = new DataTableImplV2();
      dataTable.addException(QueryException.getException(QueryException.QUERY_EXECUTION_ERROR, e));
    } finally {
      for (SegmentDataManager segmentDataManager : queryableSegmentDataManagerList) {
        tableDataManager.releaseSegment(segmentDataManager);
      }
      if (enableTrace) {
        if (dataTable != null) {
          dataTable.getMetadata().put(DataTable.TRACE_INFO_METADATA_KEY, TraceContext.getTraceInfo());
        }
        TraceContext.unregister();
      }
    }

    queryProcessingTimer.stopAndRecord();
    long queryProcessingTime = queryProcessingTimer.getDurationMs();
    dataTable.getMetadata().put(DataTable.TIME_USED_MS_METADATA_KEY, Long.toString(queryProcessingTime));
    LOGGER.debug("Query processing time for request Id - {}: {}", requestId, queryProcessingTime);
    LOGGER.debug("InstanceResponse for request Id - {}: {}", requestId, dataTable);
    return dataTable;
  }

  /**
   * Helper method to identify if a query is simple count(*) query without any predicates and group by's.
   *
   * @param brokerRequest Broker request for the query
   * @return True if simple count star query, false otherwise.
   */

  /**
   * Helper method to acquire segments that can be queried for the request.
   *
   * @param tableDataManager Table data manager
   * @param instanceRequest Instance request
   * @return List of segment data managers that can be queried.
   */
  private List<SegmentDataManager> acquireQueryableSegments(TableDataManager tableDataManager,
      final InstanceRequest instanceRequest) {
    LOGGER.debug("InstanceRequest contains {} segments", instanceRequest.getSearchSegmentsSize());

    if (tableDataManager == null || instanceRequest.getSearchSegmentsSize() == 0) {
      return new ArrayList<>();
    }
    List<SegmentDataManager> listOfQueryableSegments =
        tableDataManager.acquireSegments(instanceRequest.getSearchSegments());
    LOGGER.debug("TableDataManager found {} segments before pruning", listOfQueryableSegments.size());
    return listOfQueryableSegments;
  }

  /**
   * Helper method to prune segments.
   *
   * @param tableDataManager Table data manager
   * @param segments List of segments to prune
   * @param serverQueryRequest Server query request
   * @return Total number of docs across all segments (including the ones that were pruned).
   */
  private long pruneSegments(TableDataManager tableDataManager, List<SegmentDataManager> segments,
      ServerQueryRequest serverQueryRequest) {
    long totalRawDocs = 0;
    Iterator<SegmentDataManager> it = segments.iterator();

    while (it.hasNext()) {
      SegmentDataManager segmentDataManager = it.next();
      final IndexSegment indexSegment = segmentDataManager.getSegment();
      // We need to compute the total raw docs for the table before any pruning.
      totalRawDocs += indexSegment.getSegmentMetadata().getTotalRawDocs();
      if (_segmentPrunerService.prune(indexSegment, serverQueryRequest)) {
        it.remove();
        tableDataManager.releaseSegment(segmentDataManager);
      }
    }
    return totalRawDocs;
  }

  @Override
  public synchronized void shutDown() {
    if (isStarted()) {
      _isStarted = false;
      LOGGER.info("QueryExecutor is shutDown!");
    } else {
      LOGGER.warn("QueryExecutor is already shutDown, won't do anything!");
    }
  }

  @Override
  public boolean isStarted() {
    return _isStarted;
  }

  @Override
  public synchronized void start() {
    _isStarted = true;
    LOGGER.info("QueryExecutor is started!");
  }

  @Override
  public void updateResourceTimeOutInMs(String resource, long timeOutMs) {
    _resourceTimeOutMsMap.put(resource, timeOutMs);
  }

  private long getResourceTimeOut(BrokerRequest brokerRequest) {
    try {
      String resourceName = brokerRequest.getQuerySource().getTableName();
      if (_resourceTimeOutMsMap.containsKey(resourceName)) {
        return _resourceTimeOutMsMap.get(resourceName);
      }
    } catch (Exception e) {
      // Return the default timeout value
      LOGGER.warn("Caught exception while obtaining resource timeout", e);
    }
    return _defaultTimeOutMs;
  }
}
