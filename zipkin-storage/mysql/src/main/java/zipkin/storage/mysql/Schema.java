/**
 * Copyright 2015-2017 The OpenZipkin Authors
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
package zipkin.storage.mysql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import javax.sql.DataSource;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.Row2;
import org.jooq.SelectOffsetStep;
import zipkin.internal.Pair;
import zipkin.storage.mysql.internal.generated.tables.ZipkinAnnotations;

import static org.jooq.impl.DSL.row;
import static zipkin.storage.mysql.internal.generated.tables.ZipkinAnnotations.ZIPKIN_ANNOTATIONS;
import static zipkin.storage.mysql.internal.generated.tables.ZipkinDependencies.ZIPKIN_DEPENDENCIES;
import static zipkin.storage.mysql.internal.generated.tables.ZipkinSpans.ZIPKIN_SPANS;

final class Schema {
  final List<Field<?>> spanIdFields;
  final List<Field<?>> spanFields;
  final List<Field<?>> annotationFields;
  final List<Field<?>> dependencyLinkerFields;
  final List<Field<?>> dependencyLinkerGroupByFields;
  final List<Field<?>> dependencyLinkFields;
  final boolean hasTraceIdHigh;
  final boolean hasPreAggregatedDependencies;
  final boolean hasIpv6;
  final boolean hasErrorCount;
  final boolean strictTraceId;

  Schema(DataSource datasource, DSLContexts context, boolean strictTraceId) {
    hasTraceIdHigh = HasTraceIdHigh.test(datasource, context);
    hasPreAggregatedDependencies = HasPreAggregatedDependencies.test(datasource, context);
    hasIpv6 = HasIpv6.test(datasource, context);
    hasErrorCount = HasErrorCount.test(datasource, context);
    this.strictTraceId = strictTraceId;

    spanIdFields = list(ZIPKIN_SPANS.TRACE_ID_HIGH, ZIPKIN_SPANS.TRACE_ID);
    spanFields = list(ZIPKIN_SPANS.fields());
    annotationFields = list(ZIPKIN_ANNOTATIONS.fields());
    dependencyLinkFields = list(ZIPKIN_DEPENDENCIES.fields());
    dependencyLinkerFields = list(
        ZIPKIN_SPANS.TRACE_ID_HIGH, ZIPKIN_SPANS.TRACE_ID, ZIPKIN_SPANS.PARENT_ID,
        ZIPKIN_SPANS.ID, ZIPKIN_ANNOTATIONS.A_KEY, ZIPKIN_ANNOTATIONS.A_TYPE,
        ZIPKIN_ANNOTATIONS.ENDPOINT_SERVICE_NAME
    );
    dependencyLinkerGroupByFields = new ArrayList<>(dependencyLinkerFields);
    dependencyLinkerGroupByFields.remove(ZIPKIN_SPANS.PARENT_ID);
    if (!hasTraceIdHigh) {
      spanIdFields.remove(ZIPKIN_SPANS.TRACE_ID_HIGH);
      spanFields.remove(ZIPKIN_SPANS.TRACE_ID_HIGH);
      annotationFields.remove(ZIPKIN_ANNOTATIONS.TRACE_ID_HIGH);
      dependencyLinkerFields.remove(ZIPKIN_SPANS.TRACE_ID_HIGH);
      dependencyLinkerGroupByFields.remove(ZIPKIN_SPANS.TRACE_ID_HIGH);
    }
    if (!hasIpv6) {
      annotationFields.remove(ZIPKIN_ANNOTATIONS.ENDPOINT_IPV6);
    }
    if (!hasErrorCount) {
      dependencyLinkFields.remove(ZIPKIN_DEPENDENCIES.ERROR_COUNT);
    }
  }

  Condition joinCondition(ZipkinAnnotations annotationTable) {
    if (hasTraceIdHigh) {
      return ZIPKIN_SPANS.TRACE_ID_HIGH.eq(annotationTable.TRACE_ID_HIGH)
          .and(ZIPKIN_SPANS.TRACE_ID.eq(annotationTable.TRACE_ID))
          .and(ZIPKIN_SPANS.ID.eq(annotationTable.SPAN_ID));
    } else {
      return ZIPKIN_SPANS.TRACE_ID.eq(annotationTable.TRACE_ID)
          .and(ZIPKIN_SPANS.ID.eq(annotationTable.SPAN_ID));
    }
  }

  /** Returns a mutable list */
  static <T> List<T> list(T... elements) {
    return new ArrayList<>(Arrays.asList(elements));
  }

  Condition spanTraceIdCondition(SelectOffsetStep<? extends Record> traceIdQuery) {
    if (hasTraceIdHigh && strictTraceId) {
      Result<? extends Record> result = traceIdQuery.fetch();
      List<Row2<Long, Long>> traceIds = new ArrayList<>(result.size());
      for (Record r : result) {
        traceIds.add(row(r.get(ZIPKIN_SPANS.TRACE_ID_HIGH), r.get(ZIPKIN_SPANS.TRACE_ID)));
      }
      return row(ZIPKIN_SPANS.TRACE_ID_HIGH, ZIPKIN_SPANS.TRACE_ID).in(traceIds);
    } else {
      List<Long> traceIds = traceIdQuery.fetch(ZIPKIN_SPANS.TRACE_ID);
      return ZIPKIN_SPANS.TRACE_ID.in(traceIds);
    }
  }

  Condition spanTraceIdCondition(Long traceIdHigh, long traceIdLow) {
    return traceIdHigh != null && hasTraceIdHigh
        ? row(ZIPKIN_SPANS.TRACE_ID_HIGH, ZIPKIN_SPANS.TRACE_ID).eq(traceIdHigh, traceIdLow)
        : ZIPKIN_SPANS.TRACE_ID.eq(traceIdLow);
  }

  Condition annotationsTraceIdCondition(Set<Pair<Long>> traceIds) {
    boolean hasTraceIdHigh = false;
    for (Pair<Long> traceId : traceIds) {
      if (traceId._1 != 0) {
        hasTraceIdHigh = true;
        break;
      }
    }
    if (hasTraceIdHigh && strictTraceId) {
      Row2[] result = new Row2[traceIds.size()];
      int i = 0;
      for (Pair<Long> traceId128 : traceIds) {
        result[i++] = row(traceId128._1, traceId128._2);
      }
      return row(ZIPKIN_ANNOTATIONS.TRACE_ID_HIGH, ZIPKIN_ANNOTATIONS.TRACE_ID).in(result);
    } else {
      Long[] result = new Long[traceIds.size()];
      int i = 0;
      for (Pair<Long> traceId128 : traceIds) {
        result[i++] = traceId128._2;
      }
      return ZIPKIN_ANNOTATIONS.TRACE_ID.in(result);
    }
  }
}
