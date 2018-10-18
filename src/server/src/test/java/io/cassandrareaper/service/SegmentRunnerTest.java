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
import io.cassandrareaper.ReaperApplicationConfiguration.DatacenterAvailability;
import io.cassandrareaper.ReaperException;
import io.cassandrareaper.core.Node;
import io.cassandrareaper.core.NodeMetrics;
import io.cassandrareaper.core.RepairRun;
import io.cassandrareaper.core.RepairSegment;
import io.cassandrareaper.core.RepairUnit;
import io.cassandrareaper.jmx.JmxConnectionFactory;
import io.cassandrareaper.jmx.JmxProxy;
import io.cassandrareaper.jmx.RepairStatusHandler;
import io.cassandrareaper.storage.CassandraStorage;
import io.cassandrareaper.storage.IDistributedStorage;
import io.cassandrareaper.storage.IStorage;
import io.cassandrareaper.storage.MemoryStorage;

import java.math.BigInteger;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.cassandra.repair.RepairParallelism;
import org.apache.cassandra.service.ActiveRepairService;
import org.apache.commons.lang3.mutable.MutableObject;
import org.apache.commons.lang3.tuple.Pair;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public final class SegmentRunnerTest {
  // TODO: Clean up tests. There's a lot of code duplication across these tests.

  @Before
  public void setUp() throws Exception {
    SegmentRunner.SEGMENT_RUNNERS.clear();
  }

  @Test
  public void timeoutTest() throws InterruptedException, ReaperException, ExecutionException {
    final AppContext context = new AppContext();
    context.config = Mockito.mock(ReaperApplicationConfiguration.class);
    when(context.config.getJmxConnectionTimeoutInSeconds()).thenReturn(30);
    when(context.config.getDatacenterAvailability()).thenReturn(DatacenterAvailability.ALL);
    context.storage = new MemoryStorage();
    RepairUnit cf =
        context.storage.addRepairUnit(
            new RepairUnit.Builder(
                "reaper",
                "reaper",
                Sets.newHashSet("reaper"),
                false,
                Sets.newHashSet("127.0.0.1"),
                Collections.emptySet(),
                Collections.emptySet()));
    RepairRun run = context.storage.addRepairRun(
        new RepairRun.Builder("reaper", cf.getId(), DateTime.now(), 0.5, 1, RepairParallelism.PARALLEL),
        Collections.singleton(RepairSegment.builder(new RingRange(BigInteger.ONE, BigInteger.ZERO), cf.getId())));

    final UUID runId = run.getId();
    final UUID segmentId = context.storage.getNextFreeSegmentInRange(run.getId(), Optional.absent()).get().getId();

    final ExecutorService executor = Executors.newSingleThreadExecutor();
    final MutableObject<Future<?>> future = new MutableObject<>();

    context.jmxConnectionFactory =
        new JmxConnectionFactory() {
          @Override
          public JmxProxy connect(
              final Optional<RepairStatusHandler> handler, Node host, int connectionTimeout)
              throws ReaperException {

            JmxProxy jmx = mock(JmxProxy.class);
            when(jmx.getClusterName()).thenReturn("reaper");
            when(jmx.isConnectionAlive()).thenReturn(true);
            when(jmx.tokenRangeToEndpoint(anyString(), any(RingRange.class)))
                .thenReturn(Lists.newArrayList(""));
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
                          context.storage.getRepairSegment(runId, segmentId).get().getState());

                      future.setValue(
                          executor.submit(
                              new Thread() {
                                @Override
                                public void run() {
                                  handler
                                      .get()
                                      .handle(
                                          1,
                                          Optional.of(ActiveRepairService.Status.STARTED),
                                          Optional.absent(),
                                          "Repair command 1 has started",
                                          jmx);

                                  assertEquals(
                                      RepairSegment.State.RUNNING,
                                      context
                                          .storage
                                          .getRepairSegment(runId, segmentId)
                                          .get()
                                          .getState());
                                }
                              }));
                      return 1;
                    });

            return jmx;
          }
        };
    RepairRunner rr = mock(RepairRunner.class);
    RepairUnit ru = mock(RepairUnit.class);

    SegmentRunner sr = new SegmentRunner(
        context,
        segmentId,
        Collections.singleton(""),
        100,
        0.5,
        RepairParallelism.PARALLEL,
        "reaper",
        ru,
        rr);

    sr.run();

    future.getValue().get();
    executor.shutdown();

    assertEquals(RepairSegment.State.NOT_STARTED, context.storage.getRepairSegment(runId, segmentId).get().getState());
    assertEquals(1, context.storage.getRepairSegment(runId, segmentId).get().getFailCount());
  }

  @Test
  public void successTest() throws InterruptedException, ReaperException, ExecutionException {
    final IStorage storage = new MemoryStorage();
    RepairUnit cf =
        storage.addRepairUnit(
            new RepairUnit.Builder(
                "reaper",
                "reaper",
                Sets.newHashSet("reaper"),
                false,
                Sets.newHashSet("127.0.0.1"),
                Collections.emptySet(),
                Collections.emptySet()));
    RepairRun run = storage.addRepairRun(
        new RepairRun.Builder("reaper", cf.getId(), DateTime.now(), 0.5, 1, RepairParallelism.PARALLEL),
        Collections.singleton(RepairSegment.builder(new RingRange(BigInteger.ONE, BigInteger.ZERO), cf.getId())));
    final UUID runId = run.getId();
    final UUID segmentId = storage.getNextFreeSegmentInRange(run.getId(), Optional.absent()).get().getId();

    final ExecutorService executor = Executors.newSingleThreadExecutor();
    final MutableObject<Future<?>> future = new MutableObject<>();

    AppContext context = new AppContext();
    context.storage = storage;
    context.config = Mockito.mock(ReaperApplicationConfiguration.class);
    when(context.config.getJmxConnectionTimeoutInSeconds()).thenReturn(30);
    when(context.config.getDatacenterAvailability()).thenReturn(DatacenterAvailability.ALL);

    context.jmxConnectionFactory =
        new JmxConnectionFactory() {
          @Override
          protected JmxProxy connect(
              final Optional<RepairStatusHandler> handler, Node host, int connectionTimeout)
              throws ReaperException {

            JmxProxy jmx = mock(JmxProxy.class);
            when(jmx.getClusterName()).thenReturn("reaper");
            when(jmx.isConnectionAlive()).thenReturn(true);
            when(jmx.tokenRangeToEndpoint(anyString(), any(RingRange.class)))
                .thenReturn(Lists.newArrayList(""));
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
                    invocation -> {
                      assertEquals(
                          RepairSegment.State.NOT_STARTED,
                          storage.getRepairSegment(runId, segmentId).get().getState());

                      future.setValue(
                          executor.submit(
                              () -> {
                                handler
                                    .get()
                                    .handle(
                                        1,
                                        Optional.of(ActiveRepairService.Status.STARTED),
                                        Optional.absent(),
                                        "Repair command 1 has started",
                                        jmx);

                                assertEquals(
                                    RepairSegment.State.RUNNING,
                                    storage.getRepairSegment(runId, segmentId).get().getState());
                                // report about an unrelated repair. Shouldn't affect anything.
                                handler
                                    .get()
                                    .handle(
                                        2,
                                        Optional.of(ActiveRepairService.Status.SESSION_FAILED),
                                        Optional.absent(),
                                        "Repair command 2 has failed",
                                        jmx);

                                handler
                                    .get()
                                    .handle(
                                        1,
                                        Optional.of(ActiveRepairService.Status.SESSION_SUCCESS),
                                        Optional.absent(),
                                        "Repair session succeeded in command 1",
                                        jmx);

                                assertEquals(
                                    RepairSegment.State.DONE,
                                    storage.getRepairSegment(runId, segmentId).get().getState());

                                handler
                                    .get()
                                    .handle(
                                        1,
                                        Optional.of(ActiveRepairService.Status.FINISHED),
                                        Optional.absent(),
                                        "Repair command 1 has finished",
                                        jmx);

                                assertEquals(
                                    RepairSegment.State.DONE,
                                    storage.getRepairSegment(runId, segmentId).get().getState());
                              }));
                      return 1;
                    });

            return jmx;
          }
        };

    RepairRunner rr = mock(RepairRunner.class);
    RepairUnit ru = mock(RepairUnit.class);

    SegmentRunner sr = new SegmentRunner(
        context,
        segmentId,
        Collections.singleton(""),
        5000,
        0.5,
        RepairParallelism.PARALLEL,
        "reaper",
        ru,
        rr);

    sr.run();

    future.getValue().get();
    executor.shutdown();

    assertEquals(RepairSegment.State.DONE, storage.getRepairSegment(runId, segmentId).get().getState());
    assertEquals(0, storage.getRepairSegment(runId, segmentId).get().getFailCount());
  }

  @Test
  public void failureTest() throws InterruptedException, ReaperException, ExecutionException {
    final IStorage storage = new MemoryStorage();
    RepairUnit cf =
        storage.addRepairUnit(
            new RepairUnit.Builder(
                "reaper",
                "reaper",
                Sets.newHashSet("reaper"),
                false,
                Sets.newHashSet("127.0.0.1"),
                Collections.emptySet(),
                Collections.emptySet()));
    RepairRun run = storage.addRepairRun(
        new RepairRun.Builder("reaper", cf.getId(), DateTime.now(), 0.5, 1, RepairParallelism.PARALLEL),
        Collections.singleton(RepairSegment.builder(new RingRange(BigInteger.ONE, BigInteger.ZERO), cf.getId())));
    final UUID runId = run.getId();
    final UUID segmentId = storage.getNextFreeSegmentInRange(run.getId(), Optional.absent()).get().getId();

    final ExecutorService executor = Executors.newSingleThreadExecutor();
    final MutableObject<Future<?>> future = new MutableObject<>();

    AppContext context = new AppContext();
    context.storage = storage;
    context.config = Mockito.mock(ReaperApplicationConfiguration.class);
    when(context.config.getJmxConnectionTimeoutInSeconds()).thenReturn(30);
    when(context.config.getDatacenterAvailability()).thenReturn(DatacenterAvailability.ALL);

    context.jmxConnectionFactory =
        new JmxConnectionFactory() {
          @Override
          protected JmxProxy connect(
              final Optional<RepairStatusHandler> handler, Node host, int connectionTimeout)
              throws ReaperException {

            JmxProxy jmx = mock(JmxProxy.class);
            when(jmx.getClusterName()).thenReturn("reaper");
            when(jmx.isConnectionAlive()).thenReturn(true);
            when(jmx.tokenRangeToEndpoint(anyString(), any(RingRange.class)))
                .thenReturn(Lists.newArrayList(""));
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
                          storage.getRepairSegment(runId, segmentId).get().getState());

                      future.setValue(
                          executor.submit(
                              () -> {
                                handler
                                    .get()
                                    .handle(
                                        1,
                                        Optional.of(ActiveRepairService.Status.STARTED),
                                        Optional.absent(),
                                        "Repair command 1 has started",
                                        jmx);

                                assertEquals(
                                    RepairSegment.State.RUNNING,
                                    storage.getRepairSegment(runId, segmentId).get().getState());

                                handler
                                    .get()
                                    .handle(
                                        1,
                                        Optional.of(ActiveRepairService.Status.SESSION_FAILED),
                                        Optional.absent(),
                                        "Repair command 1 has failed",
                                        jmx);

                                assertEquals(
                                    RepairSegment.State.NOT_STARTED,
                                    storage.getRepairSegment(runId, segmentId).get().getState());

                                handler
                                    .get()
                                    .handle(
                                        1,
                                        Optional.of(ActiveRepairService.Status.FINISHED),
                                        Optional.absent(),
                                        "Repair command 1 has finished",
                                        jmx);

                                assertEquals(
                                    RepairSegment.State.NOT_STARTED,
                                    storage.getRepairSegment(runId, segmentId).get().getState());
                              }));

                      return 1;
                    });

            return jmx;
          }
        };

    RepairRunner rr = mock(RepairRunner.class);
    RepairUnit ru = mock(RepairUnit.class);

    SegmentRunner sr = new SegmentRunner(
        context,
        segmentId,
        Collections.singleton(""),
        5000,
        0.5,
        RepairParallelism.PARALLEL,
        "reaper",
        ru,
        rr);

    sr.run();

    future.getValue().get();
    executor.shutdown();

    assertEquals(RepairSegment.State.NOT_STARTED, storage.getRepairSegment(runId, segmentId).get().getState());
    assertEquals(2, storage.getRepairSegment(runId, segmentId).get().getFailCount());
  }

  @Test
  public void parseRepairIdTest() {
    String msg = "Repair session 883fd090-12f1-11e5-94c5-03d4762e50b7 for range (1,2] failed with";
    assertEquals("883fd090-12f1-11e5-94c5-03d4762e50b7", SegmentRunner.parseRepairId(msg));
    msg = "Two IDs: 883fd090-12f1-11e5-94c5-03d4762e50b7 883fd090-12f1-11e5-94c5-03d4762e50b7";
    assertEquals("883fd090-12f1-11e5-94c5-03d4762e50b7", SegmentRunner.parseRepairId(msg));
    msg = "No ID: foo bar baz";
    assertEquals(null, SegmentRunner.parseRepairId(msg));
    msg = "A message with bad ID 883fd090-fooo-11e5-94c5-03d4762e50b7";
    assertEquals(null, SegmentRunner.parseRepairId(msg));
    msg = "A message with good ID 883fd090-baad-11e5-94c5-03d4762e50b7";
    assertEquals("883fd090-baad-11e5-94c5-03d4762e50b7", SegmentRunner.parseRepairId(msg));
  }

  @Test
  public void isItOkToRepairTest() {
    assertFalse(SegmentRunner.okToRepairSegment(true, false, DatacenterAvailability.ALL));
    assertFalse(SegmentRunner.okToRepairSegment(false, false, DatacenterAvailability.ALL));
    assertTrue(SegmentRunner.okToRepairSegment(true, true, DatacenterAvailability.ALL));

    assertTrue(SegmentRunner.okToRepairSegment(true, false, DatacenterAvailability.LOCAL));
    assertFalse(SegmentRunner.okToRepairSegment(false, false, DatacenterAvailability.LOCAL));
    assertTrue(SegmentRunner.okToRepairSegment(true, true, DatacenterAvailability.LOCAL));

    assertFalse(SegmentRunner.okToRepairSegment(true, false, DatacenterAvailability.EACH));
    assertFalse(SegmentRunner.okToRepairSegment(false, false, DatacenterAvailability.EACH));
    assertTrue(SegmentRunner.okToRepairSegment(true, true, DatacenterAvailability.EACH));
  }

  @Test
  public void getTablesToRepairRemoveOneTableTest() throws ReaperException {
    JmxProxy coord = mock(JmxProxy.class);
    when(coord.getTableNamesForKeyspace(Mockito.anyString()))
        .thenReturn(Sets.newHashSet("table1", "table2", "table3"));


    RepairUnit unit = mock(RepairUnit.class);
    when(unit.getBlacklistedTables()).thenReturn(Sets.newHashSet("table1"));
    when(unit.getColumnFamilies()).thenReturn(Sets.newHashSet());
    when(unit.getKeyspaceName()).thenReturn("test");

    assertEquals(Sets.newHashSet("table2", "table3"), SegmentRunner.getTablesToRepair(coord, unit));
  }

  @Test
  public void getTablesToRepairRemoveTwoTablesTest() throws ReaperException {
    JmxProxy coord = mock(JmxProxy.class);
    when(coord.getTableNamesForKeyspace(Mockito.anyString()))
        .thenReturn(Sets.newHashSet("table1", "table2", "table3"));

    RepairUnit unit = mock(RepairUnit.class);
    when(unit.getBlacklistedTables()).thenReturn(Sets.newHashSet("table1", "table3"));
    when(unit.getColumnFamilies()).thenReturn(Sets.newHashSet());
    when(unit.getKeyspaceName()).thenReturn("test");

    assertEquals(Sets.newHashSet("table2"), SegmentRunner.getTablesToRepair(coord, unit));
  }

  @Test
  public void getTablesToRepairRemoveOneTableFromListTest() throws ReaperException {
    JmxProxy coord = mock(JmxProxy.class);
    when(coord.getTableNamesForKeyspace(Mockito.anyString()))
        .thenReturn(Sets.newHashSet("table1", "table2", "table3"));

    RepairUnit unit = mock(RepairUnit.class);
    when(unit.getBlacklistedTables()).thenReturn(Sets.newHashSet("table1"));
    when(unit.getColumnFamilies()).thenReturn(Sets.newHashSet("table1", "table2"));
    when(unit.getKeyspaceName()).thenReturn("test");

    assertEquals(Sets.newHashSet("table2"), SegmentRunner.getTablesToRepair(coord, unit));
  }

  @Test(expected = IllegalStateException.class)
  public void getTablesToRepairRemoveAllFailingTest() throws ReaperException {
    JmxProxy coord = mock(JmxProxy.class);
    when(coord.getTableNamesForKeyspace(Mockito.anyString()))
        .thenReturn(Sets.newHashSet("table1", "table2", "table3"));

    RepairUnit unit = mock(RepairUnit.class);
    when(unit.getBlacklistedTables()).thenReturn(Sets.newHashSet("table1", "table2", "table3"));
    when(unit.getColumnFamilies()).thenReturn(Sets.newHashSet());
    when(unit.getKeyspaceName()).thenReturn("test");

    SegmentRunner.getTablesToRepair(coord, unit);
  }

  @Test(expected = IllegalStateException.class)
  public void getTablesToRepairRemoveAllFromListFailingTest() throws ReaperException {
    JmxProxy coord = mock(JmxProxy.class);
    when(coord.getTableNamesForKeyspace(Mockito.anyString()))
        .thenReturn(Sets.newHashSet("table1", "table2", "table3", "table4"));

    RepairUnit unit = mock(RepairUnit.class);
    when(unit.getBlacklistedTables()).thenReturn(Sets.newHashSet("table1", "table2", "table3"));
    when(unit.getColumnFamilies()).thenReturn(Sets.newHashSet("table1", "table2", "table3"));
    when(unit.getKeyspaceName()).thenReturn("test");

    SegmentRunner.getTablesToRepair(coord, unit);
  }

  @Test
  public void getNodeMetricsInLocalDCAvailabilityForRemoteDCNodeTest() throws Exception {
    final AppContext context = new AppContext();
    context.storage = Mockito.mock(CassandraStorage.class);
    when(((IDistributedStorage) context.storage).getNodeMetrics(any(), any()))
        .thenReturn(Optional.absent());
    JmxConnectionFactory jmxConnectionFactory = mock(JmxConnectionFactory.class);
    when(jmxConnectionFactory.connect(any(), anyInt())).thenReturn(mock(JmxProxy.class));
    context.jmxConnectionFactory = jmxConnectionFactory;
    context.config = new ReaperApplicationConfiguration();
    context.config.setDatacenterAvailability(DatacenterAvailability.LOCAL);
    SegmentRunner segmentRunner = new SegmentRunner(context,UUID.randomUUID(), Collections.emptyList(),
            1000, 1.1,RepairParallelism.DATACENTER_AWARE,
            "test", mock(RepairUnit.class), mock(RepairRunner.class));

    Pair<String, Optional<NodeMetrics>> result = segmentRunner.getNodeMetrics("node-some", "dc1", "dc2").call();
    assertFalse(result.getRight().isPresent());
    verify(jmxConnectionFactory, times(0)).connect(any(), anyInt());
  }

  @Test
  public void getNodeMetricsInLocalDCAvailabilityForLocalDCNodeTest() throws Exception {
    final AppContext context = new AppContext();
    context.storage = Mockito.mock(CassandraStorage.class);

    JmxProxy proxy = mock(JmxProxy.class);
    when(proxy.getClusterName()).thenReturn("test");
    when(proxy.getPendingCompactions()).thenReturn(3);
    when(proxy.isRepairRunning()).thenReturn(true);

    JmxConnectionFactory jmxConnectionFactory = mock(JmxConnectionFactory.class);
    when(jmxConnectionFactory.connect(any(), anyInt())).thenReturn(proxy);
    context.jmxConnectionFactory = jmxConnectionFactory;

    context.config = new ReaperApplicationConfiguration();
    context.config.setDatacenterAvailability(DatacenterAvailability.LOCAL);

    SegmentRunner segmentRunner = new SegmentRunner(context,UUID.randomUUID(), Collections.emptyList(),
          1000, 1.1,RepairParallelism.DATACENTER_AWARE,
          "test", mock(RepairUnit.class), mock(RepairRunner.class));
    Pair<String, Optional<NodeMetrics>> result = segmentRunner.getNodeMetrics("node-some", "dc1", "dc1").call();
    assertTrue(result.getRight().isPresent());
    NodeMetrics metrics = result.getRight().get();
    assertEquals("test", metrics.getCluster());
    assertEquals(3, metrics.getPendingCompactions());
    assertTrue(metrics.hasRepairRunning());
  }

}
