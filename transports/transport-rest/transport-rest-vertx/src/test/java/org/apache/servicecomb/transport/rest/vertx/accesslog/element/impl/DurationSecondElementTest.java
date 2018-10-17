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

package org.apache.servicecomb.transport.rest.vertx.accesslog.element.impl;

import static org.junit.Assert.assertEquals;

import org.apache.servicecomb.transport.rest.vertx.accesslog.AccessLogParam;
import org.junit.Test;

public class DurationSecondElementTest {

  public static final DurationSecondElement ELEMENT = new DurationSecondElement();

  @Test
  public void getFormattedElementOn999ms() {
    AccessLogParam param = new AccessLogParam().setStartMillisecond(1L).setEndMillisecond(1000L);

    String result = ELEMENT.getFormattedElement(param);

    assertEquals("0", result);
  }

  @Test
  public void getFormattedElementOn1000ms() {
    AccessLogParam param = new AccessLogParam().setStartMillisecond(1L).setEndMillisecond(1001L);

    String result = ELEMENT.getFormattedElement(param);

    assertEquals("1", result);
  }

  @Test
  public void getFormattedElementOn1001ms() {
    AccessLogParam param = new AccessLogParam().setStartMillisecond(1L).setEndMillisecond(1002L);

    String result = ELEMENT.getFormattedElement(param);

    assertEquals("1", result);
  }
}
