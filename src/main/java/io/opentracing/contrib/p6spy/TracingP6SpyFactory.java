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

import com.p6spy.engine.event.JdbcEventListener;
import com.p6spy.engine.spy.P6Factory;
import com.p6spy.engine.spy.P6LoadableOptions;
import com.p6spy.engine.spy.option.P6OptionsRepository;

public class TracingP6SpyFactory implements P6Factory {

  private TracingP6SpyOptions options;

  public P6LoadableOptions getOptions(P6OptionsRepository p6OptionsRepository) {
    return options = new TracingP6SpyOptions(p6OptionsRepository);
  }

  public JdbcEventListener getJdbcEventListener() {
    return new TracingP6SpyListener(options.tracingPeerService(), options.traceWithActiveSpanOnly());
  }
}
