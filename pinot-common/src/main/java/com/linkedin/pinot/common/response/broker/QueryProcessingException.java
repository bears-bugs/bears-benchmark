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
package com.linkedin.pinot.common.response.broker;

import org.codehaus.jackson.annotate.JsonProperty;


/**
 * This class represents an exception using a message and an error code.
 */
public class QueryProcessingException {
  private int _errorCode;
  private String _message;

  public QueryProcessingException() {
  }

  public QueryProcessingException(int errorCode, String message) {
    _errorCode = errorCode;
    _message = message;
  }

  @JsonProperty("errorCode")
  public int getErrorCode() {
    return _errorCode;
  }

  @JsonProperty("errorCode")
  public void setErrorCode(int errorCode) {
    _errorCode = errorCode;
  }

  @JsonProperty("message")
  public String getMessage() {
    return _message;
  }

  @JsonProperty("message")
  public void setMessage(String message) {
    _message = message;
  }
}
