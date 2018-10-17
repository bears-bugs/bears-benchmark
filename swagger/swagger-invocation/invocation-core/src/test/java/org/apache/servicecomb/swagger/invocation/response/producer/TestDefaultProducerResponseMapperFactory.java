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
package org.apache.servicecomb.swagger.invocation.response.producer;

import javax.ws.rs.core.Response.Status;

import org.apache.servicecomb.swagger.invocation.Response;
import org.apache.servicecomb.swagger.invocation.converter.ConverterMgr;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestDefaultProducerResponseMapperFactory {
  static ConverterMgr mgr = new ConverterMgr();

  static DefaultProducerResponseMapperFactory factory = new DefaultProducerResponseMapperFactory();

  @BeforeClass
  public static void setup() {
    factory.setConverterMgr(mgr);
  }

  @Test
  public void getOrder() {
    Assert.assertEquals(Integer.MAX_VALUE, factory.getOrder());
  }

  @Test
  public void isMatch() {
    Assert.assertTrue(factory.isMatch(null, null));
  }

  @Test
  public void createResponseMapper() {
    ProducerResponseMapper mapper = factory.createResponseMapper(null, String.class, Integer.class);
    Response result = mapper.mapResponse(Status.OK, 1);

    Assert.assertSame(Status.OK, result.getStatus());
    Assert.assertEquals("1", result.getResult());
  }
}
