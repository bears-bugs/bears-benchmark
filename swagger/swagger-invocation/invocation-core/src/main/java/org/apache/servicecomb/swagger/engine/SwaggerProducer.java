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
package org.apache.servicecomb.swagger.engine;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SwaggerProducer {
  private Class<?> producerCls;

  private Class<?> swaggerIntf;

  private Map<String, SwaggerProducerOperation> opMap = new HashMap<>();

  public Class<?> getProducerCls() {
    return producerCls;
  }

  public void setProducerCls(Class<?> producerCls) {
    this.producerCls = producerCls;
  }

  public Class<?> getSwaggerIntf() {
    return swaggerIntf;
  }

  public void setSwaggerIntf(Class<?> swaggerIntf) {
    this.swaggerIntf = swaggerIntf;
  }

  public void addOperation(SwaggerProducerOperation op) {
    opMap.put(op.getName(), op);
  }

  public SwaggerProducerOperation findOperation(String name) {
    return opMap.get(name);
  }

  public Collection<SwaggerProducerOperation> getAllOperations() {
    return opMap.values();
  }
}
