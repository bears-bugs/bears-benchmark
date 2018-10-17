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

package org.apache.servicecomb.swagger.generator.core.processor.parametertype;

import org.apache.servicecomb.swagger.generator.core.OperationGenerator;
import org.apache.servicecomb.swagger.generator.core.ParameterAnnotationProcessor;
import org.apache.servicecomb.swagger.generator.core.utils.ParamUtils;

import io.swagger.models.parameters.BodyParameter;

public class RawJsonRequestBodyProcessor implements ParameterAnnotationProcessor {
  @Override
  public void process(Object annotation, OperationGenerator operationGenerator, int paramIdx) {
    BodyParameter bodyParameter = ParamUtils.createBodyParameter(operationGenerator, paramIdx);
    bodyParameter.setVendorExtension("x-raw-json", true);
    bodyParameter.setRequired(true);
    operationGenerator.addProviderParameter(bodyParameter);
  }
}
