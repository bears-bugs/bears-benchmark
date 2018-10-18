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
package com.linkedin.pinot.core.realtime.impl.dictionary;

import java.util.Arrays;
import javax.annotation.Nonnull;


public class LongOnHeapMutableDictionary extends BaseOnHeapMutableDictionary {
  private long _min = Long.MAX_VALUE;
  private long _max = Long.MIN_VALUE;

  @Override
  public int indexOf(Object rawValue) {
    if (rawValue instanceof String) {
      return getDictId(Long.valueOf((String) rawValue));
    } else {
      return getDictId(rawValue);
    }
  }

  @Override
  public void index(@Nonnull Object rawValue) {
    if (rawValue instanceof Long) {
      // Single value
      indexValue(rawValue);
      updateMinMax((Long) rawValue);
    } else {
      // Multi value
      Object[] values = (Object[]) rawValue;
      for (Object value : values) {
        indexValue(value);
        updateMinMax((Long) value);
      }
    }
  }

  @SuppressWarnings("Duplicates")
  @Override
  public boolean inRange(@Nonnull String lower, @Nonnull String upper, int dictIdToCompare, boolean includeLower,
      boolean includeUpper) {
    long lowerLong = Long.parseLong(lower);
    long upperLong = Long.parseLong(upper);
    long valueToCompare = (Long) get(dictIdToCompare);

    if (includeLower) {
      if (valueToCompare < lowerLong) {
        return false;
      }
    } else {
      if (valueToCompare <= lowerLong) {
        return false;
      }
    }

    if (includeUpper) {
      if (valueToCompare > upperLong) {
        return false;
      }
    } else {
      if (valueToCompare >= upperLong) {
        return false;
      }
    }

    return true;
  }

  @Nonnull
  @Override
  public Long getMinVal() {
    return _min;
  }

  @Nonnull
  @Override
  public Long getMaxVal() {
    return _max;
  }

  @Nonnull
  @Override
  public long[] getSortedValues() {
    int numValues = length();
    long[] sortedValues = new long[numValues];

    for (int i = 0; i < numValues; i++) {
      sortedValues[i] = (Long) get(i);
    }

    Arrays.sort(sortedValues);
    return sortedValues;
  }

  @Override
  public int getIntValue(int dictId) {
    return ((Long) get(dictId)).intValue();
  }

  @Override
  public long getLongValue(int dictId) {
    return (Long) get(dictId);
  }

  @Override
  public float getFloatValue(int dictId) {
    return (Long) get(dictId);
  }

  @Override
  public double getDoubleValue(int dictId) {
    return (Long) get(dictId);
  }

  private void updateMinMax(long value) {
    if (value < _min) {
      _min = value;
    }
    if (value > _max) {
      _max = value;
    }
  }
}
