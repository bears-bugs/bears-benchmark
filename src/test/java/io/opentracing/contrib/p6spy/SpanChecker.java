/*
 * Copyright 2017-2018 The OpenTracing Authors
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
package io.opentracing.contrib.p6spy;

import io.opentracing.mock.MockSpan;
import io.opentracing.tag.Tags;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

class SpanChecker {
  static void checkTags(Collection<MockSpan> spans, String peerService, String peerAddress) {
    for (MockSpan mockSpan : spans) {
      assertEquals(Tags.SPAN_KIND_CLIENT, mockSpan.tags().get(Tags.SPAN_KIND.getKey()));
      assertEquals("java-p6spy", mockSpan.tags().get(Tags.COMPONENT.getKey()));
      assertEquals("hsqldb", mockSpan.tags().get(Tags.DB_TYPE.getKey()));
      assertEquals("SA", mockSpan.tags().get(Tags.DB_USER.getKey()));
      assertEquals(peerService, mockSpan.tags().get(Tags.PEER_SERVICE.getKey()));
      assertEquals(peerAddress, mockSpan.tags().get("peer.address"));
      assertNotNull(mockSpan.tags().get(Tags.DB_STATEMENT.getKey()));
      assertEquals(0, mockSpan.generatedErrors().size());
    }
  }

  static void checkSameTrace(List<MockSpan> spans) {
    for (int i = 0; i < spans.size() - 1; i++) {
      assertEquals(spans.get(i).context().traceId(), spans.get(i + 1).context().traceId());
      assertEquals(spans.get(spans.size() - 1).context().spanId(), spans.get(i).parentId());
    }
  }
}
