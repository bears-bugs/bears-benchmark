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

import org.apache.servicecomb.swagger.generator.core.SwaggerGenerator;
import org.apache.servicecomb.swagger.generator.core.unittest.UnitTestSwaggerUtils;
import org.apache.servicecomb.swagger.invocation.exception.CommonExceptionData;
import org.junit.Assert;
import org.junit.Test;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;
import io.swagger.models.Operation;
import io.swagger.models.Swagger;

public class TestResponsesMeta {
  class ResponseMetaImpl {
    @ApiResponses({@ApiResponse(code = 400, response = String.class, message = ""),
        @ApiResponse(
            code = 401,
            response = long.class,
            message = "",
            responseHeaders = {@ResponseHeader(name = "h1", response = int.class)})
    })
    public int add(int x, int y) {
      return x + y;
    }
  }

  @Test
  public void test() {
    SwaggerGenerator generator = UnitTestSwaggerUtils.generateSwagger(ResponseMetaImpl.class);
    Swagger swagger = generator.getSwagger();
    Operation operation = swagger.getPath("/add").getPost();

    ResponsesMeta meta = new ResponsesMeta();
    meta.init(null, "gen", swagger, operation, int.class);

    ResponseMeta resp = meta.findResponseMeta(200);
    Assert.assertEquals(int.class, resp.getJavaType().getRawClass());

    resp = meta.findResponseMeta(201);
    Assert.assertEquals(int.class, resp.getJavaType().getRawClass());

    resp = meta.findResponseMeta(400);
    Assert.assertEquals(String.class, resp.getJavaType().getRawClass());

    resp = meta.findResponseMeta(401);
    Assert.assertEquals(Long.class, resp.getJavaType().getRawClass());
    Assert.assertEquals(Integer.class, resp.getHeaders().get("h1").getRawClass());

    resp = meta.findResponseMeta(500);
    Assert.assertEquals(CommonExceptionData.class, resp.getJavaType().getRawClass());
  }
}
