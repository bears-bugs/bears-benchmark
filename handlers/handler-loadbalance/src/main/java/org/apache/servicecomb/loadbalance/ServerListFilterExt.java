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

package org.apache.servicecomb.loadbalance;

import org.apache.servicecomb.core.Invocation;

import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerListFilter;

/**
 * 通过实现这个接口实现ServerListFilter扩展
 *
 */
public interface ServerListFilterExt extends ServerListFilter<Server> {
  default void setName(String name) {

  }

  default void setLoadBalancer(LoadBalancer lb) {

  }

  /**
   * Server list filter should be stateless. Since invocation has state information, you can't use it for next invocation.
   * Please implement stateful filters very carefully.
   */
  default void setInvocation(Invocation invocation) {

  }
}
