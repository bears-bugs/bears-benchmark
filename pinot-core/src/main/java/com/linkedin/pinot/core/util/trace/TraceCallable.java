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
package com.linkedin.pinot.core.util.trace;

import java.util.concurrent.Callable;


/**
 * Wrapper class for {@link Callable} to automatically register/un-register itself to/from a request.
 */
public abstract class TraceCallable<V> implements Callable<V> {
  private final TraceContext.TraceEntry _parentTraceEntry;

  /**
   * If trace is not enabled, parent trace entry will be null.
   */
  public TraceCallable() {
    _parentTraceEntry = TraceContext.getTraceEntry();
  }

  @Override
  public V call() throws Exception {
    if (_parentTraceEntry != null) {
      TraceContext.registerThreadToRequest(_parentTraceEntry);
    }
    try {
      return callJob();
    } finally {
      if (_parentTraceEntry != null) {
        TraceContext.unregisterThreadFromRequest();
      }
    }
  }

  public abstract V callJob() throws Exception;
}
