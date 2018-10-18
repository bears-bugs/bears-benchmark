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
package com.linkedin.pinot.core.segment.index.readers;

import com.linkedin.pinot.core.segment.memory.PinotDataBuffer;


public class FloatDictionary extends ImmutableDictionaryReader {
  private static final int FLOAT_SIZE_IN_BYTES = Float.SIZE / Byte.SIZE;

  public FloatDictionary(PinotDataBuffer dataBuffer, int length) {
    super(dataBuffer, length, FLOAT_SIZE_IN_BYTES, (byte) 0);
  }

  @Override
  public int indexOf(Object rawValue) {
    int index = insertionIndexOf(rawValue);
    return (index >= 0) ? index : -1;
  }

  @Override
  public int insertionIndexOf(Object rawValue) {
    float value;
    if (rawValue instanceof String) {
      value = Float.parseFloat((String) rawValue);
    } else {
      value = (Float) rawValue;
    }
    return binarySearch(value);
  }

  @Override
  public Float get(int dictId) {
    return getFloat(dictId);
  }

  @Override
  public int getIntValue(int dictId) {
    return (int) getFloat(dictId);
  }

  @Override
  public long getLongValue(int dictId) {
    return (long) getFloat(dictId);
  }

  @Override
  public float getFloatValue(int dictId) {
    return getFloat(dictId);
  }

  @Override
  public double getDoubleValue(int dictId) {
    return getFloat(dictId);
  }

  @Override
  public String getStringValue(int dictId) {
    return Float.toString(getFloat(dictId));
  }
}
