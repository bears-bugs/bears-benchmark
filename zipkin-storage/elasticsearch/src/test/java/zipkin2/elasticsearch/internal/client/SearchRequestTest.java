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
package zipkin2.elasticsearch.internal.client;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class SearchRequestTest {
  SearchRequest request = SearchRequest.create(asList("zipkin-2016.11.31"));
  JsonAdapter<SearchRequest> adapter = new Moshi.Builder().build().adapter(SearchRequest.class);

  @Test
  public void defaultSizeIsMaxResultWindow() {
    assertThat(request.size)
        .isEqualTo(10000);
  }

  /** Indices and type affect the request URI, not the json body */
  @Test
  public void doesntSerializeIndicesOrType() {
    assertThat(adapter.toJson(request))
        .isEqualTo("{\"size\":10000}");
  }
}
