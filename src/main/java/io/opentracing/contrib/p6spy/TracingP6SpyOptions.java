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

import com.p6spy.engine.spy.P6SpyOptions;
import com.p6spy.engine.spy.option.P6OptionsRepository;
import java.util.Map;

class TracingP6SpyOptions extends P6SpyOptions {
  private static final String PEER_SERVICE = "tracingPeerService";
  private static final String TRACE_WITH_ACTIVE_SPAN_ONLY = "traceWithActiveSpanOnly";

  private final P6OptionsRepository optionsRepository;

  TracingP6SpyOptions(P6OptionsRepository optionsRepository) {
    super(optionsRepository);
    this.optionsRepository = optionsRepository;
  }

  @Override public void load(Map<String, String> options) {
    super.load(options);

    optionsRepository.set(String.class, PEER_SERVICE, options.get(PEER_SERVICE));
    optionsRepository.set(Boolean.class, TRACE_WITH_ACTIVE_SPAN_ONLY, options.get(TRACE_WITH_ACTIVE_SPAN_ONLY));
  }

  String tracingPeerService() {
    return optionsRepository.get(String.class, PEER_SERVICE);
  }

  boolean traceWithActiveSpanOnly() {
    final Boolean traceWithActiveSpanOnly = optionsRepository.get(Boolean.class, TRACE_WITH_ACTIVE_SPAN_ONLY);
    return traceWithActiveSpanOnly != null && traceWithActiveSpanOnly;
  }
}
