/**
 * Copyright 2015-2018 The OpenZipkin Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package zipkin2.storage;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.Test;
import zipkin2.DependencyLink;
import zipkin2.Endpoint;
import zipkin2.Span;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static zipkin2.TestObjects.CLIENT_SPAN;
import static zipkin2.TestObjects.TODAY;
import static zipkin2.storage.ITSpanStore.requestBuilder;

public class InMemoryStorageTest {
  InMemoryStorage storage = InMemoryStorage.newBuilder().build();

  @Test public void getTraces_filteringMatchesMostRecentTraces() throws IOException {
    List<Endpoint> endpoints = IntStream.rangeClosed(1, 10)
      .mapToObj(i -> Endpoint.newBuilder().serviceName("service" + i).ip("127.0.0.1").build())
      .collect(Collectors.toList());

    long gapBetweenSpans = 100;
    List<Span> earlySpans =
      IntStream.rangeClosed(1, 10).mapToObj(i -> Span.newBuilder().name("early")
        .traceId(Integer.toHexString(i)).id(Integer.toHexString(i))
        .timestamp((TODAY - i) * 1000).duration(1L)
        .localEndpoint(endpoints.get(i - 1)).build()).collect(toList());

    List<Span> lateSpans = IntStream.rangeClosed(1, 10).mapToObj(i -> Span.newBuilder().name("late")
      .traceId(Integer.toHexString(i + 10)).id(Integer.toHexString(i + 10))
      .timestamp((TODAY + gapBetweenSpans - i) * 1000).duration(1L)
      .localEndpoint(endpoints.get(i - 1)).build()).collect(toList());

    storage.accept(earlySpans).execute();
    storage.accept(lateSpans).execute();

    List<Span>[] earlyTraces =
      earlySpans.stream().map(Collections::singletonList).toArray(List[]::new);
    List<Span>[] lateTraces =
      lateSpans.stream().map(Collections::singletonList).toArray(List[]::new);

    //sanity checks
    assertThat(storage.getTraces(requestBuilder().serviceName("service1").build()).execute())
      .containsExactly(lateTraces[0], earlyTraces[0]);

    assertThat(storage.getTraces(requestBuilder().build()).execute())
      .hasSize(20);

    assertThat(storage.getTraces(requestBuilder()
      .limit(10).build()).execute())
      .containsExactly(lateTraces);

    assertThat(storage.getTraces(requestBuilder()
      .endTs(TODAY + gapBetweenSpans).lookback(gapBetweenSpans).build()).execute())
      .containsExactly(lateTraces);

    assertThat(storage.getTraces(requestBuilder()
      .endTs(TODAY).build()).execute())
      .containsExactly(earlyTraces);
  }

  /** Ensures we don't overload a partition due to key equality being conflated with order */
  @Test public void differentiatesOnTraceIdWhenTimestampEqual() {
    storage.accept(asList(CLIENT_SPAN));
    storage.accept(asList(CLIENT_SPAN.toBuilder().traceId("333").build()));

    assertThat(storage).extracting("spansByTraceIdTimeStamp.delegate")
      .allSatisfy(map -> assertThat((Map) map).hasSize(2));
  }

  /** It should be safe to run dependency link jobs twice */
  @Test public void replayOverwrites() throws IOException {
    Span span = Span.newBuilder().traceId("10").id("10").name("receive")
      .kind(Span.Kind.CONSUMER)
      .localEndpoint(Endpoint.newBuilder().serviceName("app").build())
      .remoteEndpoint(Endpoint.newBuilder().serviceName("kafka").build())
      .timestamp(TODAY * 1000)
      .build();

    storage.accept(asList(span));
    storage.accept(asList(span));

    assertThat(storage.getDependencies(TODAY + 1000L, TODAY).execute()).containsOnly(
      DependencyLink.newBuilder().parent("kafka").child("app").callCount(1L).build()
    );
  }

  @Test public void getSpanNames_skipsNullSpanName() throws IOException {
    Span span1 = Span.newBuilder().traceId("1").id("1").name("root")
      .localEndpoint(Endpoint.newBuilder().serviceName("app").build())
      .timestamp(TODAY * 1000)
      .build();

    Span span2 = Span.newBuilder().traceId("1").parentId("1").id("2")
      .localEndpoint(Endpoint.newBuilder().serviceName("app").build())
      .timestamp(TODAY * 1000)
      .build();

    storage.accept(asList(span1, span2));

    assertThat(storage.getSpanNames("app").execute()).containsOnly(
      "root"
    );
  }
}
