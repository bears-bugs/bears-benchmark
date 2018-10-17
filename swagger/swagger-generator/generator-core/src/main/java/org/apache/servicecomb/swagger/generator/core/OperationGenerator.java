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

package org.apache.servicecomb.swagger.generator.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.servicecomb.swagger.SwaggerUtils;
import org.apache.servicecomb.swagger.extend.parameter.ContextParameter;
import org.apache.servicecomb.swagger.generator.core.utils.ParamUtils;
import org.springframework.util.StringUtils;

import io.swagger.models.HttpMethod;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Response;
import io.swagger.models.Swagger;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.properties.Property;
import io.swagger.util.ReflectionUtils;

public class OperationGenerator {
  protected SwaggerGenerator swaggerGenerator;

  protected Swagger swagger;

  protected Operation operation;

  // 根据方法上独立的ResponseHeader(s)标注生成的数据
  // 如果Response中不存在对应的header，则会将这些header补充进去
  protected Map<String, Property> responseHeaderMap = new HashMap<>();

  // provider的方法
  protected Method providerMethod;

  // 方法annotation所有的参数
  private List<Parameter> methodAnnotationParameters = new ArrayList<>();

  // provider所有的参数
  // 如果相同的参数名在annotationParameters中已经存在
  // 则从annotationParameters移除，将之转移到providerParameters中来，覆盖在同名位置
  private List<Parameter> providerParameters = new ArrayList<>();

  // 生成的契约参数
  private List<Parameter> swaggerParameters = new ArrayList<>();

  protected SwaggerGeneratorContext context;

  protected String path;

  protected String httpMethod;

  public OperationGenerator(SwaggerGenerator swaggerGenerator, Method providerMethod) {
    this.swaggerGenerator = swaggerGenerator;
    this.swagger = swaggerGenerator.swagger;
    this.operation = new Operation();
    this.providerMethod = providerMethod;
    this.context = swaggerGenerator.context;

    if (swagger.getParameters() != null) {
      methodAnnotationParameters.addAll(swagger.getParameters().values());
    }
  }

  public void addResponseHeader(String name, Property header) {
    responseHeaderMap.put(name, header);
  }

  public List<Parameter> getSwaggerParameters() {
    return swaggerParameters;
  }

  public SwaggerGeneratorContext getContext() {
    return context;
  }

  public SwaggerGenerator getSwaggerGenerator() {
    return swaggerGenerator;
  }

  public Swagger getSwagger() {
    return swagger;
  }

  public Operation getOperation() {
    return operation;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    path = context.resolveStringValue(path);
    if (!path.startsWith("/")) {
      path = "/" + path;
    }
    this.path = path;
  }

  public String getHttpMethod() {
    return httpMethod;
  }

  public void setHttpMethod(String httpMethod) {
    if (StringUtils.isEmpty(httpMethod)) {
      return;
    }

    this.httpMethod = httpMethod.toLowerCase(Locale.US);
  }

  public void addMethodAnnotationParameter(Parameter parameter) {
    methodAnnotationParameters.add(parameter);
  }

  public void addProviderParameter(Parameter parameter) {
    providerParameters.add(parameter);
  }

  public List<Parameter> getProviderParameters() {
    return providerParameters;
  }

  public void generate() {
    scanMethodAnnotation();
    scanMethodParameters();
    scanResponse();

    checkBodyParameter();
    copyToSwaggerParameters();
    operation.setParameters(swaggerParameters);

    correctOperation();
  }

  protected void copyToSwaggerParameters() {
    for (Parameter parameter : providerParameters) {
      if (ContextParameter.class.isInstance(parameter)) {
        continue;
      }

      int annotationIdx = ParamUtils.findParameterByName(parameter.getName(), methodAnnotationParameters);
      if (annotationIdx != -1) {
        Parameter annotationParameter = methodAnnotationParameters.remove(annotationIdx);
        swaggerParameters.add(annotationParameter);
        continue;
      }

      swaggerParameters.add(parameter);
    }

    swaggerParameters.addAll(methodAnnotationParameters);
  }

  protected int countRealBodyParameter(List<Parameter> parameters) {
    int count = 0;
    for (Parameter p : parameters) {
      if (ParamUtils.isRealBodyParameter(p)) {
        count++;
      }
    }

    return count;
  }

  protected void checkBodyParameter() {
    // annotationParameters中不能有多个body
    int annotationBodyCount = countRealBodyParameter(methodAnnotationParameters);
    if (annotationBodyCount > 1) {
      throw new Error(String.format("too many (%d) body parameter in %s:%s annotation",
          annotationBodyCount,
          providerMethod.getDeclaringClass().getName(),
          providerMethod.getName()));
    }

    // providerParameters中不能有多个body
    int parameterBodyCount = countRealBodyParameter(providerParameters);
    if (parameterBodyCount > 1) {
      throw new Error(String.format("too many (%d) body parameter in %s:%s parameters",
          parameterBodyCount,
          providerMethod.getDeclaringClass().getName(),
          providerMethod.getName()));
    }

    // annotationParameters和providerParameters不能同时出现body
    if (annotationBodyCount + parameterBodyCount >= 2) {
      throw new Error(String.format("not allow both defined body parameter in %s:%s annotation and parameters",
          providerMethod.getDeclaringClass().getName(),
          providerMethod.getName()));
    }
  }

  protected void scanMethodAnnotation() {
    for (Annotation annotation : providerMethod.getAnnotations()) {
      MethodAnnotationProcessor processor = context.findMethodAnnotationProcessor(annotation.annotationType());
      if (processor == null) {
        continue;
      }
      processor.process(annotation, this);
    }

    setDefaultTag();
  }

  private void setDefaultTag() {
    // if tag has been defined, do nothing
    if (null != operation.getTags()) {
      for (String tag : operation.getTags()) {
        if (!StringUtils.isEmpty(tag)) {
          return;
        }
      }
    }

    // if there is no tag, set default tag
    if (!swaggerGenerator.getDefaultTags().isEmpty()) {
      operation.setTags(new ArrayList<>(swaggerGenerator.getDefaultTags()));
    }
  }

  /**
   *
   * 根据method上的数据，综合生成契约参数
   */
  protected void scanMethodParameters() {
    Annotation[][] allAnnotations = providerMethod.getParameterAnnotations();
    Type[] parameterTypes = providerMethod.getGenericParameterTypes();
    for (int paramIdx = 0; paramIdx < parameterTypes.length; paramIdx++) {
      int swaggerParamCount = providerParameters.size();

      // 根据annotation处理
      Annotation[] paramAnnotations = allAnnotations[paramIdx];
      processByParameterAnnotation(paramAnnotations, paramIdx);

      if (isArgumentNotProcessed(swaggerParamCount)) {
        Type type = parameterTypes[paramIdx];
        // 是否需要根据参数类型处理，目标场景：httpRequest之类
        processByParameterType(type, paramIdx);
      }

      if (isArgumentNotProcessed(swaggerParamCount)) {
        // 没有用于描述契约的标注，根据函数原型来反射生成
        context.getDefaultParamProcessor().process(this, paramIdx);
      }
    }
  }

  private boolean isArgumentNotProcessed(int swaggerParamCount) {
    return swaggerParamCount == providerParameters.size();
  }

  protected void processByParameterAnnotation(Annotation[] paramAnnotations, int paramIdx) {
    for (Annotation annotation : paramAnnotations) {
      ParameterAnnotationProcessor processor =
          context.findParameterAnnotationProcessor(annotation.annotationType());
      if (processor != null) {
        processor.process(annotation, this, paramIdx);
      }
    }
  }

  protected void processByParameterType(Type parameterType, int paramIdx) {
    ParameterTypeProcessor processor = context.findParameterTypeProcessor(parameterType);
    if (processor != null) {
      processor.process(this, paramIdx);
    }
  }

  public void correctOperation() {
    if (StringUtils.isEmpty(operation.getOperationId())) {
      operation.setOperationId(providerMethod.getName());
    }

    context.postProcessOperation(this);

    SwaggerUtils.correctResponses(operation);
    addHeaderToResponse();
  }

  private void addHeaderToResponse() {
    for (Entry<String, Response> responseEntry : operation.getResponses().entrySet()) {
      Response response = responseEntry.getValue();

      for (Entry<String, Property> entry : responseHeaderMap.entrySet()) {
        if (response.getHeaders() != null && response.getHeaders().containsKey(entry.getKey())) {
          continue;
        }

        response.addHeader(entry.getKey(), entry.getValue());
      }
    }
  }

  public void scanResponse() {
    if (operation.getResponses() != null) {
      Response successResponse = operation.getResponses().get(SwaggerConst.SUCCESS_KEY);
      if (successResponse != null) {
        if (successResponse.getSchema() == null) {
          // 标注已经定义了response，但是是void，这可能是在标注上未定义
          // 根据函数原型来处理response
          Property property = createResponseProperty();
          successResponse.setSchema(property);
        }
        return;
      }
    }

    Property property = createResponseProperty();
    Response response = new Response();
    response.setSchema(property);
    operation.addResponse(SwaggerConst.SUCCESS_KEY, response);
  }

  protected Property createResponseProperty() {
    Type responseType = providerMethod.getReturnType();
    if (ReflectionUtils.isVoid(responseType)) {
      return null;
    }

    ResponseTypeProcessor processor = context.findResponseTypeProcessor(responseType);
    return processor.process(this);
  }

  public Method getProviderMethod() {
    return providerMethod;
  }

  protected void addOperationToSwagger() {
    if (StringUtils.isEmpty(httpMethod)) {
      return;
    }

    Path pathObj = swagger.getPath(path);
    if (pathObj == null) {
      pathObj = new Path();
      swagger.path(path, pathObj);
    }

    HttpMethod hm = HttpMethod.valueOf(httpMethod.toUpperCase(Locale.US));
    if (pathObj.getOperationMap().get(hm) != null) {
      throw new Error(String.format("Only allowed one default path. %s:%s",
          swaggerGenerator.getCls().getName(),
          providerMethod.getName()));
    }
    pathObj.set(httpMethod, operation);
  }
}
