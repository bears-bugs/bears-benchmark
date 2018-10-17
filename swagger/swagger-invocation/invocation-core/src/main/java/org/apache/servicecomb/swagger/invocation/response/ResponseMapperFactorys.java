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
package org.apache.servicecomb.swagger.invocation.response;

import java.lang.reflect.Type;
import java.util.List;

import org.apache.servicecomb.foundation.common.utils.SPIServiceUtils;
import org.apache.servicecomb.swagger.invocation.converter.ConverterMgr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResponseMapperFactorys<MAPPER> {
  private static final Logger LOGGER = LoggerFactory.getLogger(ResponseMapperFactorys.class);

  private List<ResponseMapperFactory<MAPPER>> factorys;

  public ResponseMapperFactorys(Class<? extends ResponseMapperFactory<MAPPER>> factoryCls, ConverterMgr converterMgr) {
    this(factoryCls);
    this.setConverterMgr(converterMgr);
  }

  @SuppressWarnings("unchecked")
  public ResponseMapperFactorys(Class<? extends ResponseMapperFactory<MAPPER>> factoryCls) {
    factorys = (List<ResponseMapperFactory<MAPPER>>) SPIServiceUtils.getSortedService(factoryCls);
    factorys.forEach(factory -> {
      LOGGER.info("found factory {} of {}:", factory.getClass().getName(), factoryCls.getName());
    });
  }

  public void setConverterMgr(ConverterMgr converterMgr) {
    factorys.forEach(factory -> factory.setConverterMgr(converterMgr));
  }

  public MAPPER createResponseMapper(Type swaggerType, Type providerType) {
    for (ResponseMapperFactory<MAPPER> factory : factorys) {
      if (!factory.isMatch(swaggerType, providerType)) {
        continue;
      }

      return factory.createResponseMapper(this, swaggerType, providerType);
    }

    throw new IllegalStateException(
        String.format("can not find response mapper for %s and %s, this should never happened.",
            swaggerType.getTypeName(),
            providerType.getTypeName()));
  }
}
