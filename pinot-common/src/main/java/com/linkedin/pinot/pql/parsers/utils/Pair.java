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
package com.linkedin.pinot.pql.parsers.utils;

import java.io.Serializable;


public class Pair<FIRST extends Serializable, SECOND extends Serializable> implements Serializable {
  private FIRST first;
  private SECOND second;

  public FIRST getFirst() {
    return first;
  }

  public void setFirst(FIRST first) {
    this.first = first;
  }

  public SECOND getSecond() {
    return second;
  }

  public void setSecond(SECOND second) {
    this.second = second;
  }

  public Pair(FIRST first, SECOND second) {
    super();
    this.first = first;
    this.second = second;
  }
}
