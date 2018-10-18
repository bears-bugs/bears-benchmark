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
package com.linkedin.pinot.core.segment.creator;

/**
 * Abstract base class implementation for {@link SingleValueRawIndexCreator}
 */
public abstract class BaseSingleValueRawIndexCreator implements SingleValueRawIndexCreator {
  @Override
  public void index(int docId, int valueToIndex) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void index(int docId, long valueToIndex) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void index(int docId, float valueToIndex) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void index(int docId, double valueToIndex) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void index(int docId, String valueToIndex) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void index(int docId, byte[] valueToIndex) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void index(int docId, Object valueToIndex) {
    throw new UnsupportedOperationException();
  }
}
