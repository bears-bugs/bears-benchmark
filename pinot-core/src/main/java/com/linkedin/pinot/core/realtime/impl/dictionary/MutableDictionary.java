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

import com.linkedin.pinot.core.segment.index.readers.BaseDictionary;
import javax.annotation.Nonnull;


public abstract class MutableDictionary extends BaseDictionary {

  @Override
  public String getStringValue(int dictId) {
    return get(dictId).toString();
  }

  public boolean isSorted() {
    return false;
  }

  public abstract void index(@Nonnull Object rawValue);

  public abstract boolean inRange(@Nonnull String lower, @Nonnull String upper, int dictIdToCompare,
      boolean includeLower, boolean includeUpper);

  @Nonnull
  public abstract Object getMinVal();

  @Nonnull
  public abstract Object getMaxVal();

  @Nonnull
  public abstract Object getSortedValues();

  public abstract int getAvgValueSize();

  public abstract boolean isEmpty();

}
