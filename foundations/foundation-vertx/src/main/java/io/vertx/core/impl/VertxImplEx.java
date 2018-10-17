/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.vertx.core.impl;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;

public class VertxImplEx extends VertxImpl {
  private AtomicLong eventLoopContextCreated = new AtomicLong();

  public VertxImplEx(String name, VertxOptions vertxOptions) {
    super(vertxOptions);

    if (StringUtils.isEmpty(name)) {
      return;
    }

    Field field = ReflectionUtils.findField(VertxImpl.class, "eventLoopThreadFactory");
    field.setAccessible(true);
    VertxThreadFactory eventLoopThreadFactory = (VertxThreadFactory) ReflectionUtils.getField(field, this);

    field = ReflectionUtils.findField(eventLoopThreadFactory.getClass(), "prefix");
    field.setAccessible(true);

    String prefix = (String) ReflectionUtils.getField(field, eventLoopThreadFactory);
    ReflectionUtils.setField(field, eventLoopThreadFactory, name + "-" + prefix);
  }

  @Override
  public EventLoopContext createEventLoopContext(String deploymentID, WorkerPool workerPool, JsonObject config,
      ClassLoader tccl) {
    eventLoopContextCreated.incrementAndGet();
    return super.createEventLoopContext(deploymentID, workerPool, config, tccl);
  }

  public long getEventLoopContextCreatedCount() {
    return eventLoopContextCreated.get();
  }
}
