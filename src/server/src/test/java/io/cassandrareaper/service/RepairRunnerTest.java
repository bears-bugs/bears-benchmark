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

package io.cassandrareaper.service;

import io.cassandrareaper.AppContext;
import io.cassandrareaper.ReaperApplicationConfiguration;
import io.cassandrareaper.ReaperException;
import io.cassandrareaper.core.Cluster;
import io.cassandrareaper.core.Node;
import io.cassandrareaper.core.RepairRun;
import io.cassandrareaper.core.RepairSegment;
import io.cassandrareaper.core.RepairUnit;
import io.cassandrareaper.jmx.JmxConnectionFactory;
import io.cassandrareaper.jmx.JmxProxy;
import io.cassandrareaper.jmx.RepairStatusHandler;
import io.cassandrareaper.storage.IStorage;
import io.cassandrareaper.storage.MemoryStorage;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.cassandra.repair.RepairParallelism;
import org.apache.cassandra.service.ActiveRepairService;
import org.apache.cassandra.utils.progress.ProgressEventType;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class RepairRunnerTest {

  private static final Logger LOG = LoggerFactory.getLogger(RepairRunnerTest.class);

  @Before
  public void setUp() throws Exception {
    SegmentRunner.SEGMENT_RUNNERS.clear();
  }

  @Test
  public void testHangingRepair() throws InterruptedException, ReaperException {
    final String CLUSTER_NAME = "reaper";
    final String KS_NAME = "reaper";
    final Set<String> CF_NAMES = Sets.newHashSet("reaper");
    final boolean INCREMENTAL_REPAIR = false;
    final Set<String> NODES = Sets.newHashSet("127.0.0.1");
    final Set<String> DATACENTERS = Collections.emptySet();
    final Set<String> BLACKLISTED_TABLES = Collections.emptySet();
    final long TIME_RUN = 41L;
    final double INTENSITY = 0.5f;

    final IStorage storage = new MemoryStorage();

    storage.addCluster(new Cluster(CLUSTER_NAME, null, Collections.<String>singleton("127.0.0.1")));
    RepairUnit cf =
        storage.addRepairUnit(
            new RepairUnit.Builder(CLUSTER_NAME, KS_NAME, CF_NAMES, INCREMENTAL_REPAIR, NODES,
                DATACENTERS, BLACKLISTED_TABLES));
    DateTimeUtils.setCurrentMillisFixed(TIME_RUN);
    RepairRun run = storage.addRepairRun(
        new RepairRun.Builder(CLUSTER_NAME, cf.getId(), DateTime.now(), INTENSITY, 1, RepairParallelism.PARALLEL),
        Collections.singleton(RepairSegment.builder(new RingRange(BigInteger.ZERO, BigInteger.ONE), cf.getId())));
    final UUID RUN_ID = run.getId();
    final UUID SEGMENT_ID = storage.getNextFreeSegmentInRange(run.getId(), Optional.absent()).get().getId();

    assertEquals(storage.getRepairSegment(RUN_ID, SEGMENT_ID).get().getState(), RepairSegment.State.NOT_STARTED);
    AppContext context = new AppContext();
    context.storage = storage;
    context.config = new ReaperApplicationConfiguration();
    context.repairManager = RepairManager.create(context);
    context.repairManager.initializeThreadPool(1, 500, TimeUnit.MILLISECONDS, 1, TimeUnit.MILLISECONDS);

    final Semaphore mutex = new Semaphore(0);

    context.jmxConnectionFactory =
        new JmxConnectionFactory() {
          final AtomicInteger repairAttempts = new AtomicInteger(1);

          @Override
          protected JmxProxy connect(
              final Optional<RepairStatusHandler> handler, Node host, int connectionTimeout)
              throws ReaperException {

            final JmxProxy jmx = mock(JmxProxy.class);
            when(jmx.getClusterName()).thenReturn(CLUSTER_NAME);
            when(jmx.isConnectionAlive()).thenReturn(true);
            when(jmx.tokenRangeToEndpoint(anyString(), any(RingRange.class)))
                .thenReturn(Lists.newArrayList(""));
            when(jmx.getRangeToEndpointMap(anyString()))
                .thenReturn(RepairRunnerTest.sixNodeCluster());
            when(jmx.getDataCenter()).thenReturn("dc1");
            when(jmx.getDataCenter(anyString())).thenReturn("dc1");

            when(jmx.triggerRepair(any(BigInteger.class), any(BigInteger.class),
                any(), any(RepairParallelism.class), any(), anyBoolean(), any(), any()))
                .then(
                    (invocation) -> {
                      assertEquals(
                          RepairSegment.State.NOT_STARTED,
                          storage.getRepairSegment(RUN_ID, SEGMENT_ID).get().getState());
                      final int repairNumber = repairAttempts.getAndIncrement();
                      switch (repairNumber) {
                        case 1:
                          new Thread() {
                            @Override
                            public void run() {
                              handler
                                  .get()
                                  .handle(repairNumber, Optional.of(ActiveRepairService.Status.STARTED),
                                      Optional.absent(), null, jmx);

                              assertEquals(
                                  RepairSegment.State.RUNNING,
                                  storage.getRepairSegment(RUN_ID, SEGMENT_ID).get().getState());
                            }
                          }.start();
                          break;
                        case 2:
                          new Thread() {
                            @Override
                            public void run() {

                              handler
                                  .get()
                                  .handle(repairNumber, Optional.of(ActiveRepairService.Status.STARTED),
                                      Optional.absent(), null, jmx);

                              assertEquals(
                                  RepairSegment.State.RUNNING,
                                  storage.getRepairSegment(RUN_ID, SEGMENT_ID).get().getState());

                              handler
                                  .get()
                                  .handle(repairNumber, Optional.of(ActiveRepairService.Status.SESSION_SUCCESS),
                                      Optional.absent(), null, jmx);

                              assertEquals(
                                  RepairSegment.State.DONE,
                                  storage.getRepairSegment(RUN_ID, SEGMENT_ID).get().getState());

                              handler
                                  .get()
                                  .handle(repairNumber, Optional.of(ActiveRepairService.Status.FINISHED),
                                      Optional.absent(), null, jmx);

                              mutex.release();
                              LOG.info("MUTEX RELEASED");
                            }
                          }.start();
                          break;
                        default:
                          fail("triggerRepair should only have been called twice");
                      }
                      LOG.info("repair number : " + repairNumber);
                      return repairNumber;
                    });
            return jmx;
          }
        };
    context.repairManager.startRepairRun(run);

    await().with().atMost(20, TimeUnit.SECONDS).until(()
        -> {
      try {
        mutex.acquire();
        LOG.info("MUTEX ACQUIRED");
        // TODO: refactor so that we can properly wait for the repair runner to finish rather than
        // TODO: using this sleep().
        Thread.sleep(1000);
        return true;
      } catch (InterruptedException ex) {
        throw new IllegalStateException(ex);
      }
    });
    assertEquals(RepairRun.RunState.DONE, storage.getRepairRun(RUN_ID).get().getRunState());
  }

  @Test
  public void testHangingRepairNewAPI() throws InterruptedException, ReaperException {
    final String CLUSTER_NAME = "reaper";
    final String KS_NAME = "reaper";
    final Set<String> CF_NAMES = Sets.newHashSet("reaper");
    final boolean INCREMENTAL_REPAIR = false;
    final Set<String> NODES = Sets.newHashSet("127.0.0.1");
    final Set<String> DATACENTERS = Collections.emptySet();
    final Set<String> BLACKLISTED_TABLES = Collections.emptySet();
    final long TIME_RUN = 41L;
    final double INTENSITY = 0.5f;

    final IStorage storage = new MemoryStorage();

    storage.addCluster(new Cluster(CLUSTER_NAME, null, Collections.<String>singleton("127.0.0.1")));
    RepairUnit cf =
        storage.addRepairUnit(
            new RepairUnit.Builder(
                CLUSTER_NAME,
                KS_NAME,
                CF_NAMES,
                INCREMENTAL_REPAIR,
                NODES,
                DATACENTERS,
                BLACKLISTED_TABLES));
    DateTimeUtils.setCurrentMillisFixed(TIME_RUN);
    RepairRun run = storage.addRepairRun(
        new RepairRun.Builder(CLUSTER_NAME, cf.getId(), DateTime.now(), INTENSITY, 1, RepairParallelism.PARALLEL),
        Collections.singleton(RepairSegment.builder(new RingRange(BigInteger.ZERO, BigInteger.ONE), cf.getId())));
    final UUID RUN_ID = run.getId();
    final UUID SEGMENT_ID = storage.getNextFreeSegmentInRange(run.getId(), Optional.absent()).get().getId();

    assertEquals(storage.getRepairSegment(RUN_ID, SEGMENT_ID).get().getState(), RepairSegment.State.NOT_STARTED);
    AppContext context = new AppContext();
    context.storage = storage;
    context.config = new ReaperApplicationConfiguration();
    context.repairManager = RepairManager.create(context);
    context.repairManager.initializeThreadPool(1, 500, TimeUnit.MILLISECONDS, 1, TimeUnit.MILLISECONDS);

    final Semaphore mutex = new Semaphore(0);

    context.jmxConnectionFactory =
        new JmxConnectionFactory() {
          final AtomicInteger repairAttempts = new AtomicInteger(1);

          @Override
          protected JmxProxy connect(
              final Optional<RepairStatusHandler> handler, Node host, int connectionTimeout)
              throws ReaperException {

            final JmxProxy jmx = mock(JmxProxy.class);
            when(jmx.getClusterName()).thenReturn(CLUSTER_NAME);
            when(jmx.isConnectionAlive()).thenReturn(true);
            when(jmx.tokenRangeToEndpoint(anyString(), any(RingRange.class)))
                .thenReturn(Lists.newArrayList(""));
            when(jmx.getRangeToEndpointMap(anyString()))
                .thenReturn(RepairRunnerTest.sixNodeCluster());
            when(jmx.getDataCenter()).thenReturn("dc1");
            when(jmx.getDataCenter(anyString())).thenReturn("dc1");
            //doNothing().when(jmx).cancelAllRepairs();

            when(jmx.triggerRepair(any(BigInteger.class), any(BigInteger.class),
                any(), any(RepairParallelism.class), any(), anyBoolean(), any(), any()))
                .then(
                    (invocation) -> {
                      assertEquals(
                          RepairSegment.State.NOT_STARTED,
                          storage.getRepairSegment(RUN_ID, SEGMENT_ID).get().getState());

                      final int repairNumber = repairAttempts.getAndIncrement();
                      switch (repairNumber) {
                        case 1:
                          new Thread() {
                            @Override
                            public void run() {
                              handler
                                  .get()
                                  .handle(repairNumber, Optional.absent(),
                                      Optional.of(ProgressEventType.START), null, jmx);
                              assertEquals(
                                  RepairSegment.State.RUNNING,
                                  storage.getRepairSegment(RUN_ID, SEGMENT_ID).get().getState());
                            }
                          }.start();
                          break;
                        case 2:
                          new Thread() {
                            @Override
                            public void run() {
                              handler
                                  .get()
                                  .handle(repairNumber, Optional.absent(),
                                      Optional.of(ProgressEventType.START), null, jmx);
                              assertEquals(
                                  RepairSegment.State.RUNNING,
                                  storage.getRepairSegment(RUN_ID, SEGMENT_ID).get().getState());
                              handler
                                  .get()
                                  .handle(repairNumber, Optional.absent(),
                                      Optional.of(ProgressEventType.SUCCESS), null, jmx);
                              assertEquals(
                                  RepairSegment.State.DONE,
                                  storage.getRepairSegment(RUN_ID, SEGMENT_ID).get().getState());
                              handler
                                  .get()
                                  .handle(repairNumber, Optional.absent(),
                                      Optional.of(ProgressEventType.COMPLETE), null, jmx);
                              mutex.release();
                              LOG.info("MUTEX RELEASED");
                            }
                          }.start();
                          break;
                        default:
                          fail("triggerRepair should only have been called twice");
                      }
                      return repairNumber;
                    });
            return jmx;
          }
        };
    context.repairManager.startRepairRun(run);

    await().with().atMost(20, TimeUnit.SECONDS).until(()
        -> {
      try {
        mutex.acquire();
        LOG.info("MUTEX ACQUIRED");
        // TODO: refactor so that we can properly wait for the repair runner to finish rather than
        // TODO: using this sleep().
        Thread.sleep(1000);
        return true;
      } catch (InterruptedException ex) {
        throw new IllegalStateException(ex);
      }
    });
    assertEquals(RepairRun.RunState.DONE, storage.getRepairRun(RUN_ID).get().getRunState());
  }

  @Test
  public void testResumeRepair() throws InterruptedException, ReaperException {
    final String CLUSTER_NAME = "reaper";
    final String KS_NAME = "reaper";
    final Set<String> CF_NAMES = Sets.newHashSet("reaper");
    final boolean INCREMENTAL_REPAIR = false;
    final Set<String> NODES = Sets.newHashSet("127.0.0.1");
    final Set<String> DATACENTERS = Collections.emptySet();
    final Set<String> BLACKLISTED_TABLES = Collections.emptySet();
    final long TIME_RUN = 41L;
    final double INTENSITY = 0.5f;

    final IStorage storage = new MemoryStorage();
    AppContext context = new AppContext();
    context.storage = storage;
    context.config = new ReaperApplicationConfiguration();
    context.repairManager = RepairManager.create(context);

    storage.addCluster(new Cluster(CLUSTER_NAME, null, Collections.<String>singleton("127.0.0.1")));
    UUID cf =
        storage
            .addRepairUnit(
                new RepairUnit.Builder(
                    CLUSTER_NAME,
                    KS_NAME,
                    CF_NAMES,
                    INCREMENTAL_REPAIR,
                    NODES,
                    DATACENTERS,
                    BLACKLISTED_TABLES))
            .getId();
    DateTimeUtils.setCurrentMillisFixed(TIME_RUN);

    RepairRun run = storage.addRepairRun(
        new RepairRun.Builder(CLUSTER_NAME, cf, DateTime.now(), INTENSITY, 1, RepairParallelism.PARALLEL),
        Lists.newArrayList(
            RepairSegment.builder(new RingRange(BigInteger.ZERO, BigInteger.ONE), cf)
                .withState(RepairSegment.State.RUNNING)
                .withStartTime(DateTime.now())
                .withCoordinatorHost("reaper"),
            RepairSegment.builder(new RingRange(BigInteger.ONE, BigInteger.ZERO), cf)));

    final UUID RUN_ID = run.getId();
    final UUID SEGMENT_ID = storage.getNextFreeSegmentInRange(run.getId(), Optional.absent()).get().getId();

    context.repairManager.initializeThreadPool(1, 500, TimeUnit.MILLISECONDS, 1, TimeUnit.MILLISECONDS);

    assertEquals(storage.getRepairSegment(RUN_ID, SEGMENT_ID).get().getState(), RepairSegment.State.NOT_STARTED);
    context.jmxConnectionFactory =
        new JmxConnectionFactory() {
          @Override
          protected JmxProxy connect(
              final Optional<RepairStatusHandler> handler, Node host, int connectionTimeout)
              throws ReaperException {

            final JmxProxy jmx = mock(JmxProxy.class);
            when(jmx.getClusterName()).thenReturn(CLUSTER_NAME);
            when(jmx.isConnectionAlive()).thenReturn(true);
            when(jmx.tokenRangeToEndpoint(anyString(), any(RingRange.class)))
                .thenReturn(Lists.newArrayList(""));
            when(jmx.getRangeToEndpointMap(anyString()))
                .thenReturn(RepairRunnerTest.sixNodeCluster());
            when(jmx.getDataCenter()).thenReturn("dc1");
            when(jmx.getDataCenter(anyString())).thenReturn("dc1");

            when(jmx.triggerRepair(
                    any(BigInteger.class),
                    any(BigInteger.class),
                    any(),
                    any(RepairParallelism.class),
                    any(),
                    anyBoolean(),
                    any(),
                    any()))
                .then(
                    (invocation) -> {
                      assertEquals(
                          RepairSegment.State.NOT_STARTED,
                          storage.getRepairSegment(RUN_ID, SEGMENT_ID).get().getState());

                      new Thread() {
                        @Override
                        public void run() {
                          handler
                              .get()
                              .handle(
                                  1,
                                  Optional.of(ActiveRepairService.Status.STARTED),
                                  Optional.absent(),
                                  null,
                                  jmx);

                          handler
                              .get()
                              .handle(
                                  1,
                                  Optional.of(ActiveRepairService.Status.SESSION_SUCCESS),
                                  Optional.absent(),
                                  null,
                                  jmx);

                          handler
                              .get()
                              .handle(
                                  1,
                                  Optional.of(ActiveRepairService.Status.FINISHED),
                                  Optional.absent(),
                                  null,
                                  jmx);
                        }
                      }.start();
                      return 1;
                    });
            return jmx;
          }
        };

    assertEquals(RepairRun.RunState.NOT_STARTED, storage.getRepairRun(RUN_ID).get().getRunState());
    context.repairManager.resumeRunningRepairRuns();
    assertEquals(RepairRun.RunState.NOT_STARTED, storage.getRepairRun(RUN_ID).get().getRunState());
    storage.updateRepairRun(run.with().runState(RepairRun.RunState.RUNNING).build(RUN_ID));
    context.repairManager.resumeRunningRepairRuns();
    Thread.sleep(1000);
    assertEquals(RepairRun.RunState.DONE, storage.getRepairRun(RUN_ID).get().getRunState());
  }

  @Test
  public void getPossibleParallelRepairsTest() throws Exception {
    Map<List<String>, List<String>> map = RepairRunnerTest.threeNodeCluster();
    Map<String, String> endpointsThreeNodes = RepairRunnerTest.threeNodeClusterEndpoint();
    assertEquals(1, RepairRunner.getPossibleParallelRepairsCount(map, endpointsThreeNodes));

    map = RepairRunnerTest.sixNodeCluster();
    Map<String, String> endpointsSixNodes = RepairRunnerTest.sixNodeClusterEndpoint();
    assertEquals(2, RepairRunner.getPossibleParallelRepairsCount(map, endpointsSixNodes));
  }

  @Test
  public void getParallelSegmentsTest() throws ReaperException {
    List<BigInteger> tokens = Lists
        .transform(Lists.newArrayList("0", "50", "100", "150", "200", "250"), (string) -> new BigInteger(string));

    SegmentGenerator generator = new SegmentGenerator(new BigInteger("0"), new BigInteger("299"));
    List<RingRange> segments = generator.generateSegments(32, tokens, Boolean.FALSE);

    Map<List<String>, List<String>> map = RepairRunnerTest.sixNodeCluster();
    Map<String, String> endpointsSixNodes = RepairRunnerTest.sixNodeClusterEndpoint();
    List<RingRange> ranges = RepairRunner.getParallelRanges(
        RepairRunner.getPossibleParallelRepairsCount(map, endpointsSixNodes),
        segments
    );
    assertEquals(2, ranges.size());
    assertEquals("0", ranges.get(0).getStart().toString());
    assertEquals("150", ranges.get(0).getEnd().toString());
    assertEquals("150", ranges.get(1).getStart().toString());
    assertEquals("0", ranges.get(1).getEnd().toString());
  }

  @Test
  public void getParallelSegmentsTest2() throws ReaperException {
    List<BigInteger> tokens = Lists.transform(
        Lists.newArrayList("0", "25", "50", "75", "100", "125", "150", "175", "200", "225", "250"),
        (string) -> new BigInteger(string));

    SegmentGenerator generator = new SegmentGenerator(new BigInteger("0"), new BigInteger("299"));
    List<RingRange> segments = generator.generateSegments(32, tokens, Boolean.FALSE);

    Map<List<String>, List<String>> map = RepairRunnerTest.sixNodeCluster();
    Map<String, String> endpointsSixNodes = RepairRunnerTest.sixNodeClusterEndpoint();
    List<RingRange> ranges = RepairRunner.getParallelRanges(
        RepairRunner.getPossibleParallelRepairsCount(map, endpointsSixNodes),
        segments
    );
    assertEquals(2, ranges.size());
    assertEquals("0", ranges.get(0).getStart().toString());
    assertEquals("150", ranges.get(0).getEnd().toString());
    assertEquals("150", ranges.get(1).getStart().toString());
    assertEquals("0", ranges.get(1).getEnd().toString());
  }

  public static Map<List<String>, List<String>> threeNodeCluster() {
    Map<List<String>, List<String>> map = Maps.newHashMap();
    map = addRangeToMap(map, "0", "50", "a1", "a2", "a3");
    map = addRangeToMap(map, "50", "100", "a2", "a3", "a1");
    map = addRangeToMap(map, "100", "0", "a3", "a1", "a2");
    return map;
  }

  public static Map<List<String>, List<String>> sixNodeCluster() {
    Map<List<String>, List<String>> map = Maps.newLinkedHashMap();
    map = addRangeToMap(map, "0", "50", "a1", "a2", "a3");
    map = addRangeToMap(map, "50", "100", "a2", "a3", "a4");
    map = addRangeToMap(map, "100", "150", "a3", "a4", "a5");
    map = addRangeToMap(map, "150", "200", "a4", "a5", "a6");
    map = addRangeToMap(map, "200", "250", "a5", "a6", "a1");
    map = addRangeToMap(map, "250", "0", "a6", "a1", "a2");
    return map;
  }

  public static Map<String, String> threeNodeClusterEndpoint() {
    Map<String, String> map = Maps.newHashMap();
    map.put("host1", "hostId1");
    map.put("host2", "hostId2");
    map.put("host3", "hostId3");
    return map;
  }

  public static Map<String, String> sixNodeClusterEndpoint() {
    Map<String, String> map = Maps.newHashMap();
    map.put("host1", "hostId1");
    map.put("host2", "hostId2");
    map.put("host3", "hostId3");
    map.put("host4", "hostId4");
    map.put("host5", "hostId5");
    map.put("host6", "hostId6");
    return map;
  }

  private static Map<List<String>, List<String>> addRangeToMap(
      Map<List<String>, List<String>> map,
      String start,
      String end,
      String... hosts) {

    List<String> range = Lists.newArrayList(start, end);
    List<String> endPoints = Lists.newArrayList(hosts);
    map.put(range, endPoints);
    return map;
  }

}
