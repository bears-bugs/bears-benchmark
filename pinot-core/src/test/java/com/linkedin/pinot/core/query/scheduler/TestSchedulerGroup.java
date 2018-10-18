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
package com.linkedin.pinot.core.query.scheduler;

import java.util.concurrent.ConcurrentLinkedQueue;
import javax.annotation.Nonnull;


class TestSchedulerGroup extends AbstractSchedulerGroup {

  TestSchedulerGroup(@Nonnull String name) {
    super(name);
  }

  private ConcurrentLinkedQueue<SchedulerQueryContext> getQueue() {
    return pendingQueries;
  }

  @Override
  public int compareTo(SchedulerGroupAccountant o) {
    int lhs = Integer.parseInt(name);
    int rhs = Integer.parseInt(((TestSchedulerGroup) o).name);
    if (lhs < rhs) {
      return 1;
    } else if (lhs > rhs) {
      return -1;
    }
    return 0;
  }
}
