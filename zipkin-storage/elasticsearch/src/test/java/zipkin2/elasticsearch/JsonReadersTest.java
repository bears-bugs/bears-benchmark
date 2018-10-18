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
package zipkin2.elasticsearch;

import com.squareup.moshi.JsonReader;
import java.io.IOException;
import java.util.List;
import okio.Buffer;
import org.junit.Test;
import zipkin2.elasticsearch.internal.JsonReaders;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonReadersTest {

  @Test public void enterPath_nested() throws IOException {
    assertThat(JsonReaders.enterPath(JsonReader.of(new Buffer().writeUtf8("{\n"
      + "  \"name\" : \"Kamal\",\n"
      + "  \"cluster_name\" : \"elasticsearch\",\n"
      + "  \"version\" : {\n"
      + "    \"number\" : \"2.4.0\",\n"
      + "    \"build_hash\" : \"ce9f0c7394dee074091dd1bc4e9469251181fc55\",\n"
      + "    \"build_timestamp\" : \"2016-08-29T09:14:17Z\",\n"
      + "    \"build_snapshot\" : false,\n"
      + "    \"lucene_version\" : \"5.5.2\"\n"
      + "  },\n"
      + "  \"tagline\" : \"You Know, for Search\"\n"
      + "}")), "version", "number").nextString())
      .isEqualTo("2.4.0");
  }

  @Test public void enterPath_nullOnNoInput() throws IOException {
    assertThat(JsonReaders.enterPath(JsonReader.of(new Buffer()), "message"))
      .isNull();
  }

  @Test public void collectValuesNamed_emptyWhenNotFound() throws IOException {
    List<String> result = JsonReaders.collectValuesNamed(JsonReader.of(new Buffer().writeUtf8(
      "{\"took\":1,\"timed_out\":false,\"_shards\":{\"total\":0,\"successful\":0,\"failed\":0},\"hits\":{\"total\":0,\"max_score\":0.0,\"hits\":[]}}"
    )), "key");

    assertThat(result).isEmpty();
  }

  @Test public void collectValuesNamed_mergesArrays() throws IOException {
    List<String> result =
      JsonReaders.collectValuesNamed(
        JsonReader.of(new Buffer().writeUtf8(TestResponses.SPAN_NAMES)), "key");

    assertThat(result).containsExactly("methodcall", "yak");
  }

  @Test public void collectValuesNamed_mergesChildren() throws IOException {
    List<String> result = JsonReaders.collectValuesNamed(JsonReader.of(new Buffer().writeUtf8(
      TestResponses.SERVICE_NAMES)), "key");

    assertThat(result).containsExactly("yak", "service");
  }

  @Test public void collectValuesNamed_nested() throws IOException {
    List<String> result = JsonReaders.collectValuesNamed(JsonReader.of(new Buffer().writeUtf8("{\n"
      + "  \"took\": 49,\n"
      + "  \"timed_out\": false,\n"
      + "  \"_shards\": {\n"
      + "    \"total\": 5,\n"
      + "    \"successful\": 5,\n"
      + "    \"failed\": 0\n"
      + "  },\n"
      + "  \"hits\": {\n"
      + "    \"total\": 1,\n"
      + "    \"max_score\": 0,\n"
      + "    \"hits\": []\n"
      + "  },\n"
      + "  \"aggregations\": {\n"
      + "    \"traceId_agg\": {\n"
      + "      \"doc_count_error_upper_bound\": 0,\n"
      + "      \"sum_other_doc_count\": 0,\n"
      + "      \"buckets\": [\n"
      + "        {\n"
      + "          \"key\": \"000000000000007b\",\n"
      + "          \"doc_count\": 1,\n"
      + "          \"timestamps_agg\": {\n"
      + "            \"value\": 1474761600001,\n"
      + "            \"value_as_string\": \"1474761600001\"\n"
      + "          }\n"
      + "        }\n"
      + "      ]\n"
      + "    }\n"
      + "  }\n"
      + "}")), "key");

    assertThat(result).containsExactly("000000000000007b");
  }
}
